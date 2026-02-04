通过 Reactor Context + Hooks 对 reactive operator 进行 MDC 桥接，使得由 reactive 链调度执行的代码在日志中自动携带 traceId / spanId；

在 WebFilter 中将 traceId 写入 ServerWebExchange，作为 Web 层请求级上下文，用于 ExceptionHandler 等最终边界稳定取值；

在命令式 UseCase 方法中，仅在入口、出口及外部 port 调用前后记录生命周期日志，不引入额外上下文或 phase 体系。
