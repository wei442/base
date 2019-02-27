package com.ochain.provider.redis.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;
import com.ochain.provider.redis.boot.BootRestMapResponse;
import com.ochain.provider.redis.rest.request.RedisRequest;
import com.ochain.provider.redis.vo.WithScoresVo;

import redis.clients.jedis.Tuple;

/**
 * Redis SortedSet（有序集合） BootRedisSortedSetController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/sortedSet")
public class BootRedisSortedSetController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO SortedSet(有序集合) 命令工具方法
	/************************** jredis SortedSet(有序集合) 命令工具方法 ****************************/
	/**
	 * 根据分数值存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据分数值存储有序集合】(BootRedisSortedSetController-zadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double score = req.getScore();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(score == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "score为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zadd(key, score, member);
			logger.info("===step2:【根据分数值存储有序集合】(BootRedisSortedSetController-zadd)-存储有序集合-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【根据分数值存储有序集合】(BootRedisSortedSetController-zadd)-存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【根据分数值存储有序集合】(BootRedisSortedSetController-zadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 有序集key的基数
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zcard",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zcard(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key的基数】(BootRedisSortedSetController-zcard)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.zcard(key);
			logger.info("===step2:【有序集key的基数】(BootRedisSortedSetController-zcard)-有序集key的基数-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key的基数】(BootRedisSortedSetController-zcard)-成员的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集key的基数】(BootRedisSortedSetController-zcard)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zcount",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zcount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中成员的数量】(BootRedisSortedSetController-zcount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(min == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "min为空");
		} else if(max == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "max为空");
		}

		long len = 0l;
		try {
			len = redisService.zcount(key, min, max);
			logger.info("===step2:【有序集key中成员的数量】(BootRedisSortedSetController-zcount)-成员的数量-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中成员的数量】(BootRedisSortedSetController-zcount)-成员的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集key中成员的数量】(BootRedisSortedSetController-zcount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 有序集合成员分数增加增量
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zincrby",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zincrby(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集合成员分数增加增量】(BootRedisSortedSetController-zincrby)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double score = req.getScore();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(score == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "score为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		double len = 0d;
		try {
			len = redisService.zincrby(key, score, member);
			logger.info("===step2:【有序集合成员分数增加增量】(BootRedisSortedSetController-zincrby)-存储有序集合-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集合成员分数增加增量】(BootRedisSortedSetController-zincrby)-存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集合成员分数增加增量】(BootRedisSortedSetController-zincrby)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递增(从小到大)来排序。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrange",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(start == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "start为空");
		} else if(end == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "end为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrange(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrange)-指定区间内的成员-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrange)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递增(从小到大)来排序。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrangeWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrangeWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrangeWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(start == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "start为空");
		} else if(end == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "end为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrangeWithScores(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrangeWithScores)-指定区间内的成员-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrangeWithScores)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}
		List<WithScoresVo> result = null;
		WithScoresVo withScoresVo = null;
		if(tuples != null && !tuples.isEmpty()) {
			Iterator<Tuple> it = tuples.iterator();
			result = new ArrayList<WithScoresVo>();
			while (it.hasNext()) {
				Tuple tuple = it.next();
				String element = tuple.getElement();
				double score = tuple.getScore();

				withScoresVo = new WithScoresVo();
				withScoresVo.setElement(element);
				withScoresVo.setScore(score);
				result.add(withScoresVo);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrangeWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据分数值范围查询存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrangeByScore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrangeByScore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(min == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "min为空");
		} else if(max == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "max为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrangeByScore(key, min, max);
			logger.info("===step2:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScore)-查询存储有序集合-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScore)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据分数值范围查询存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrangeByScoreWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrangeByScoreWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScoreWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(min == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "min为空");
		} else if(max == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "max为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrangeByScoreWithScores(key, min, max);
			logger.info("===step2:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScoreWithScores)-查询存储有序集合-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScoreWithScores)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}
		List<WithScoresVo> result = null;
		WithScoresVo withScoresVo = null;
		if(tuples != null && !tuples.isEmpty()) {
			Iterator<Tuple> it = tuples.iterator();
			result = new ArrayList<WithScoresVo>();
			while (it.hasNext()) {
				Tuple tuple = it.next();
				String element = tuple.getElement();
				double score = tuple.getScore();

				withScoresVo = new WithScoresVo();
				withScoresVo.setElement(element);
				withScoresVo.setScore(score);
				result.add(withScoresVo);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【根据分数值范围查询存储有序集合】(BootRedisSortedSetController-zrangeByScoreWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrank",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrank(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集中成员排名】(BootRedisSortedSetController-zrank)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zrank(key, member);
			logger.info("===step2:【有序集中成员排名】(BootRedisSortedSetController-zrank)-成员排名-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集中成员排名】(BootRedisSortedSetController-zrank)-成员排名-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集中成员排名】(BootRedisSortedSetController-zrank)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrem",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrem(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除有序集key中的一个成员】(BootRedisSortedSetController-zrem)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zrem(key, member);
			logger.info("===step2:【移除有序集key中的一个成员】(BootRedisSortedSetController-zrem)-移除一个成员-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除有序集key中的一个成员】(BootRedisSortedSetController-zrem)-移除一个成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除有序集key中的一个成员】(BootRedisSortedSetController-zrem)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除有序集 key 中的多个成员，不存在的成员将被忽略。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/zrem",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zremArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除有序集key中的多个成员】(BootRedisSortedSetController-zremArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(members == null || members.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "members为空");
		}

		long len = 0l;
		try {
			len = redisService.zrem(key, members);
			logger.info("===step2:【移除有序集key中的多个成员】(BootRedisSortedSetController-zremArray)-移除多个成员-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除有序集key中的多个成员】(BootRedisSortedSetController-zremArray)-移除多个成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除有序集key中的多个成员】(BootRedisSortedSetController-zremArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zremrangeByRank",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zremrangeByRank(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除指定排名区间内的所有成员】(BootRedisSortedSetController-zremrangeByRank)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(start == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "start为空");
		} else if(end == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "end为空");
		}

		long len = 0l;
		try {
			len = redisService.zremrangeByRank(key, start, end);
			logger.info("===step2:【移除指定排名区间内的所有成员】(BootRedisSortedSetController-zremrangeByRank)-移除指定排名区间内的成员-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除指定排名区间内的所有成员】(BootRedisSortedSetController-zremrangeByRank)-移除指定排名区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除指定排名区间内的所有成员】(BootRedisSortedSetController-zremrangeByRank)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 分数值范围删除存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zremrangeByScore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zremrangeByScore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【分数值范围删除存储有序集合】(BootRedisSortedSetController-zremrangeByScore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(min == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "min为空");
		} else if(max == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "max为空");
		}

		long len = 0l;
		try {
			len = redisService.zremrangeByScore(key, min, max);
			logger.info("===step2:【分数值范围删除存储有序集合】(BootRedisSortedSetController-zremrangeByScore)-删除存储有序集合-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【分数值范围删除存储有序集合】(BootRedisSortedSetController-zrangeByScore)-删除存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【分数值范围删除存储有序集合】(BootRedisSortedSetController-zremrangeByScore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递减(从大到小)来排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrevrange",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrevrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(start == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "start为空");
		} else if(end == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "end为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrevrange(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrange)-指定区间内的成员-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrange)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递减(从大到小)来排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrevrangeWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrevrangeWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrangeWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(start == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "start为空");
		} else if(end == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "end为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrevrangeWithScores(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrangeWithScores)-指定区间内的成员-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrangeWithScores)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}
		List<WithScoresVo> result = null;
		WithScoresVo withScoresVo = null;
		if(tuples != null && !tuples.isEmpty()) {
			Iterator<Tuple> it = tuples.iterator();
			result = new ArrayList<WithScoresVo>();
			while (it.hasNext()) {
				Tuple tuple = it.next();
				String element = tuple.getElement();
				double score = tuple.getScore();

				withScoresVo = new WithScoresVo();
				withScoresVo.setElement(element);
				withScoresVo.setScore(score);
				result.add(withScoresVo);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(BootRedisSortedSetController-zrevrangeWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrevrangeByScore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrevrangeByScore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(min == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "min为空");
		} else if(max == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "max为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrevrangeByScore(key, max, min);
			logger.info("===step2:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScore)-查询存储有序集合-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScore)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrevrangeByScoreWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrevrangeByScoreWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScoreWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(min == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "min为空");
		} else if(max == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "max为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrevrangeByScoreWithScores(key, max, min);
			logger.info("===step2:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScoreWithScores)-查询存储有序集合-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScoreWithScores)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}
		List<WithScoresVo> result = null;
		WithScoresVo withScoresVo = null;
		if(tuples != null && !tuples.isEmpty()) {
			Iterator<Tuple> it = tuples.iterator();
			result = new ArrayList<WithScoresVo>();
			while (it.hasNext()) {
				Tuple tuple = it.next();
				String element = tuple.getElement();
				double score = tuple.getScore();

				withScoresVo = new WithScoresVo();
				withScoresVo.setElement(element);
				withScoresVo.setScore(score);
				result.add(withScoresVo);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中max和min之间所有的成员】(BootRedisSortedSetController-zrevrangeByScoreWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集key中成员member的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zrevrank",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zrevrank(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回有序集key中成员member的排名】(BootRedisSortedSetController-zrevrank)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zrevrank(key, member);
			logger.info("===step2:【返回有序集key中成员member的排名】(BootRedisSortedSetController-zrevrank)-成员member排名-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【【返回有序集key中成员member的排名】(BootRedisSortedSetController-zrevrank)-成员member排名-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回有序集key中成员member的排名】(BootRedisSortedSetController-zrevrank)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集key中，成员member的score 值。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/zscore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse zscore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中成员member的score值】(BootRedisSortedSetController-zscore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		double len = 0d;
		try {
			len = redisService.zscore(key, member);
			logger.info("===step2:【有序集key中成员member的score值】(BootRedisSortedSetController-zscore)-成员score值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【有序集key中成员member的score值】(BootRedisSortedSetController-zscore)-成员score值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集key中成员member的score值】(BootRedisSortedSetController-zscore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/************************** jredis SortedSet(有序集合) 命令工具方法 ****************************/

}