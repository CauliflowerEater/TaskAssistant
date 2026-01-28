Java对象

任务分析结果。
列表中每个元素代表一个任务；success 仅在 LLM 输出通过 schema 校验且语义完整时返回；任意校验失败均视为 failure。

每个任务对象包含 4 个字段：
- name: 任务名（string）
- detail: 任务详情（string）
- tags: 标签（int[]），每个任务最多三个 tag

