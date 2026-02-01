【架构目标】
- 系统采用“分层异构模型”：
  - Web / API 层：响应式（Reactive / WebFlux / Mono / Flux）
  - Application / Domain / Service 层：命令式（Imperative / Blocking）
- 响应式模型 **严格限制在 Web 层**，不得向下渗透。

【核心约束（必须遵守）】
1. Web 层职责
   - 负责：
     - 协议适配（HTTP / Streaming / SSE 等）
     - Reactive 到 Imperative 的边界转换
     - 线程切换与阻塞隔离（如 boundedElastic）
   - Web 层可以使用：
     - Mono / Flux
     - map / flatMap / onErrorResume 等 Reactor API
   - Web 层必须在边界处完成：
     - `.publishOn(boundedElastic)` 或等价手段
     - `.fromCallable(...)` / `.defer(...)` 封装阻塞调用

2. 业务层（Application / Domain / Service）
   - 必须是 **纯命令式**
   - 方法签名禁止返回：
     - Mono / Flux / Publisher / Reactive 类型
   - 允许：
     - 普通返回值
     - 抛出异常
     - 阻塞式 I/O（DB / RPC / MQ）
   - 业务逻辑不得感知：
     - Reactor Context
     - Backpressure
     - 非阻塞调度模型
     
3. 边界原则（非常重要）
   - Reactive → Imperative 的转换：
     - 只能发生在 Web / Adapter 层
   - Imperative → Reactive 的包装：
     - 只能是“薄包装”，不得重写业务语义
   - 禁止：
     - 在业务层调用 `.block()`
     - 在业务层引入 Reactor / WebFlux 依赖
     - 为了“响应式”而拆散业务事务边界

4. 事务与一致性
   - 事务边界只存在于命令式业务层
   - 不使用 Reactive Transaction Manager
   - Web 层不得承诺事务语义，仅做请求受理与结果映射