package com.example.projectservice.aop;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAspect {

	private final RedissonClient redissonClient;

	@Around("@annotation(redissonLock)")
	public Object redissonLock(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
		String lockKey = redissonLock.lockKey();
		RLock lock = redissonClient.getLock(lockKey);

		boolean isLocked = false;
		try {
			isLocked = lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), TimeUnit.SECONDS);
			if (!isLocked) {
				log.warn("Failed to acquire lock for key: {}", lockKey);
				return null;
			}

			return joinPoint.proceed();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Interrupted while trying to acquire lock for key: {}", lockKey, e);
			return null;
		} finally {
			if (isLocked && lock.isHeldByCurrentThread())
				lock.unlock();
		}
	}
}
