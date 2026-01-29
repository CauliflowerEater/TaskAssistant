type: port
id: TaskAnalyzeLLMPort                           


summary: >
  提供“基于配置中的分析配置进行任务分析”的能力：输入自然语言文本，输出任务分析结果；
  失败时以统一的技术失败模型返回原因（reason）与结果确定性（certainty），供上层进行语义裁决与重试决策。

offer:

  clauses:
    P1:
      title: schema 来自配置

    P2:
      title: 成功结果满足 schema
      desc: >
        成功时返回的结构化结果必须满足 schema 约束（字段/类型/必填项等）。
        返回形态必须稳定（例如稳定返回“已解析对象”或稳定返回“可解析 JSON 文本”之一）。
      guarantees:
        - 成功输出可被上层直接消费，无需再次进行schema/格式层面的结构化校验
      non_guarantees:
        - 不保证业务语义正确（例如字段值是否“合理”由上层策略决定）

    P3:
      title: 统一技术失败模型（单一异常类型）
      desc: >
        任意失败均以统一的技术失败模型 TaskAnalyzeLLMPortFailure 对上层暴露（单一异常类型或等价失败结果），
        通过字段表达失败原因与可确认性，禁止向上泄漏 SDK 原始异常/HTTP 状态码等实现细节。
      guarantees:
        - 任意技术失败均以 TaskAnalyzeLLMPortFailure 对上层暴露（单一异常类型或等价失败结果）
        - TaskAnalyzeLLMPortFailure 的字段结构以 ./errors/TaskAnalyzeLLMPortFailure.yaml 为唯一权威定义
        - 禁止向上泄漏 SDK/HTTP 状态码等实现细节
      non_guarantees:
        - 不承诺 failure.reason 与业务 failureSemantic 的一一对应（由上层语义裁决）

    P4:
      title: 内部重试仅限结构化输出违例（有限次）
      desc: >
        port 的实现允许在“下游已返回输出但结构化校验未通过”的情形下进行有限次内部重试；
        除此之外的失败（如 TIMEOUT / RATE_LIMITED / DEPENDENCY_FAILURE / UNKNOWN）不进行内部重试。
      guarantees:
        - 当 reason=STRUCTURED_OUTPUT_VIOLATION 时，可在单次调用内最多重试 maxAttempts 次（默认建议 3）
        - 对外暴露的 failure.attempts / failure.maxAttempts 反映已尝试次数
      non_guarantees:
        - 不保证内部重试一定成功；若最终失败，将以 TaskAnalyzeLLMPortFailure 暴露最终失败原因

    P5:
      title: 可观测性最低承诺（本地关联）
      desc: >
        port 应支持获取 traceId 并用于本地日志关联；是否向下游透传 traceId 不在本 port 承诺范围内。
      guarantees:
        - 失败模型中包含 traceId（若上下文提供）
      non_guarantees:
        - 不保证跨系统链路追踪能力（仅保证本系统内可关联）

  # 2) API 形态（方法清单）
  # 注意：这里只定义“语义 API”，不是实现 API
  operations:
    - name: analyze_task
      input:
        - name: rawText
          type: string
          required: true
          desc: 自然语言输入文本（例如用户描述的待分析任务）
      output:
        success:
          type: TaskAnalysisLLMResult
          ref: ./dto/TaskAnalysisLLMResult.md
          desc: 任务分析结果
        failure:
          type: TaskAnalyzeLLMPortFailure
          ref: ./errors/TaskAnalyzeLLMPortFailure.yaml
      effects:
        - kind: call_llm
          desc: 调用外部模型服务完成任务分析
      semantics:
        idempotency:
          supported: false
          notes: >
            本 port 不在语义层承诺幂等；幂等/重试安全性由上层结合幂等域保证与策略决定。
        consistency:
          level: unknown
          notes: >
            本 port 不承诺输出结果的业务一致性，仅承诺任务分析结果的形态与失败信号。
        concurrency:
          conflict_detectable: false
          exposed_as:
            kind: none
            value: n/a
          notes: >
            本 port 不承诺也不暴露“并发冲突”的可检测语义：并发调用被视为独立请求。
            若上层需要并发去重/互斥/同 key 等待复用，应通过幂等/互斥类 port 在上层编排实现。
      exceptions:
        - id: TaskAnalyzeLLMPortFailure
          ref: ./errors/TaskAnalyzeLLMPortFailure.yaml
          when: 调用失败或结构化校验失败时（见 failure.reason 与 failure.certainty 字段）
          retryable: false

  # 4) 通用约束（对所有 operations 生效）
  constraints:
    input_validation:
      minimal_defensive_checks: true
      notes: >
        port 对明显无效输入执行防御性校验，但不进行业务语义判定
    retry_semantics:
      caller_may_retry: true
      safe_when: []
      notes: >
        本 port 不对调用方重试的安全性做承诺或禁止；仅提供可识别的技术失败语义（reason/certainty/attempts），
        上层应结合幂等域、退避策略与业务语义自行决定是否重试。
    observability:
      trace_required: true
      metrics: []
      logs:
        pii: prohibited
        redaction: required

assumption:
  # port 通常不需要 assumption
  # 若存在，仅限于对调用方行为的最低假设（例如必须提供 key）
  caller_assumptions:
    - rawText 为非空文本；若为空或仅空白，port 可直接返回输入校验失败（作为 TaskAnalyzeLLMPortFailure.reason=INVALID_INPUT）

# NOTE:
# Port 层不包含 Implementation Policy。
# Port 只声明“能力语义的承诺（Offer）”与最小调用方前置条件（Assumption），
# 任何实现策略、存储选型、并发控制、重试、幂等实现等，均属于 Infra/Adapter 层职责。

rollout:
  compatibility: compatible               # compatible | breaking
  migration_notes: >
    v1 初始版本，无迁移事项。

notes:
  - <anything else>