# TaskAnalysisRequestDTO

type: dto  
id: TaskAnalysisRequestDTO  
version: v1  
summary: 任务拆分请求参数

## fields

- name: userId
  - type: string
  - required: true
  - desc: 用户ID
  - constraints:
    - not_blank: true
    - max_length: 64
- name: idempotencyKey
  - type: string
  - required: true
  - desc: 幂等键
  - constraints:
    - not_blank: true
    - max_length: 128
- name: taskText
  - type: string
  - required: true
  - desc: 任务文本，长度上限 1000 字符
  - constraints:
    - not_blank: true
    - max_length: 1000
- name: schemaId
  - type: string
  - required: true
  - desc: 任务schema ID，用于唯一标识任务schema，由config决定
  - constraints:
    - not_blank: true
    - max_length: 64
