幂等表在系统中的定位是：

1. 对于API调用的幂等去重；
2. 充当producer和worker之间的消息队列；
3. 充当结果的缓存；

幂等表的字段：

- id
- task_id
- scope
- idem_key
- status
- created_at, updated_at, finished_at
- owner_id, lease_util
- execute_id
- attempts, next_retry_at
- result_payload
- error_code, error_message
- expire_at
- trace_id

id 为自增的逻辑主键；
task_id为业务键，调用方在发起创建请求后服务端会返回task_id，后调用方持task_id轮询执行的结果；

scope为幂等域，设计上是userId;
idem_key是由调用方提供的幂等键，在scope内唯一；
scope+idem_key需满足unique约束；

status为状态机，见下文状态机设计；

owner_id为消费worker的id，设计上worker需要将自己的worker_id cas写入记录用于站位；
lease_util为worker占用记录的租期，在worker抢占时同时写入；

execute_id是worker请求AI服务后由AI服务端返回的业务id，worker会用这个execute_id向AI服务端轮询，该id会被抢锁worker复用；

attempts代表当前记录的消费尝试次数，初始为0，每取锁成功一次加一；
next_retry_at 待定，或许会取消

result_payload worker在消费幂等记录后会将结果回写到result_payload；

error_code代表约定的异常字段
error_message为截断的异常信息（前100字符），用于向前暴露；

expire_at字段用用于表示幂等记录的过期时间；

trace_id 观测字段；
