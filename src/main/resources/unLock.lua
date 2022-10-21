local key = KEYS[1];
local threadId = ARGV[1];

-- lockName ,threadId 不存在
if(redis.call('hexists',key,threadId) == 0)then
    return nil;
end;

-- 计数器减1
local count = redis.call('hincrby',key,threadId,-1);

-- 如果等于0 ，删除key
if(count == 0)then
    redis.call('del',key);
    return nil;
end;