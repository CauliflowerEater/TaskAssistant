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
- owner_id, lease_util, lease_epoch
- execute_id
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
lease_until为worker占用记录的租期，在worker抢占时同时写入；
lease_epoch表示锁轮次，每次抢锁成功时递增，用于防止旧轮次异步回调晚到写回污染当前状态, 同时用于退避策略计算；

execute_id是worker请求AI服务后由AI服务端返回的业务id，worker会用这个execute_id向AI服务端轮询，该id会被抢锁worker复用；

result_payload worker在消费幂等记录后会将结果回写到result_payload；

error_code代表约定的异常字段
error_message为截断的异常信息（前100字符），用于向前暴露；

expire_at字段用用于表示幂等记录的过期时间；

trace_id 观测字段；

状态机status字段：

- PENDING
- PROCESSING
- SUCCESS
- FAILED
- TIMEOUT
  PENDING -> PROCESSING
  PROCESSING -> SUCCESS
  PROCESSING -> FAILED
  PROCESSING -> TIMEOUT
  TIMEOUT / FAILED / SUCCESS 为终态，不可逆
