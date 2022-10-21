local key = KEYS[1];
local threadId = ARGV[1];
local timeOut = ARGV[2];
-- lockName 不存在
if(redis.call('exists',key) == 0) then
    redis.call('hset',key,threadId,1);
    redis.call('expire',key,timeOut);
    return 1;
end;
-- lockName 已存在
if(redis.call('hexists',key,threadId) == 1) then
    redis.call('hincrby',key,threadId,1);
    redis.call('expire',key,timeOut);
    return 1;
end;
return 0;