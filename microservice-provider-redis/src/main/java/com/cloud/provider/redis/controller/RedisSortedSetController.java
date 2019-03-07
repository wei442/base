package com.cloud.provider.redis.controller;

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
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.enums.RedisResultEnum;
import com.cloud.provider.redis.exception.RedisException;
import com.cloud.provider.redis.rest.request.RedisRequest;
import com.cloud.provider.redis.vo.WithScoresVo;

import redis.clients.jedis.Tuple;

/**
 * Redis SortedSet（有序集合） RedisSortedSetController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/sortedSet")
public class RedisSortedSetController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO SortedSet(有序集合) 命令工具方法
	/************************** jredis SortedSet(有序集合) 命令工具方法 ****************************/
	/**
	 * 根据分数值存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据分数值存储有序集合】(RedisSortedSetController-zadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double score = req.getScore();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(score == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "score为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zadd(key, score, member);
			logger.info("===step2:【根据分数值存储有序集合】(RedisSortedSetController-zadd)-存储有序集合-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【根据分数值存储有序集合】(RedisSortedSetController-zadd)-存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【根据分数值存储有序集合】(RedisSortedSetController-zadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 有序集key的基数
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zcard",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zcard(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key的基数】(RedisSortedSetController-zcard)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.zcard(key);
			logger.info("===step2:【有序集key的基数】(RedisSortedSetController-zcard)-有序集key的基数-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key的基数】(RedisSortedSetController-zcard)-成员的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集key的基数】(RedisSortedSetController-zcard)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zcount",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zcount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中成员的数量】(RedisSortedSetController-zcount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(min == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "min为空");
		} else if(max == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "max为空");
		}

		long len = 0l;
		try {
			len = redisService.zcount(key, min, max);
			logger.info("===step2:【有序集key中成员的数量】(RedisSortedSetController-zcount)-成员的数量-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中成员的数量】(RedisSortedSetController-zcount)-成员的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集key中成员的数量】(RedisSortedSetController-zcount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 有序集合成员分数增加增量
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zincrby",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zincrby(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集合成员分数增加增量】(RedisSortedSetController-zincrby)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double score = req.getScore();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(score == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "score为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		double len = 0d;
		try {
			len = redisService.zincrby(key, score, member);
			logger.info("===step2:【有序集合成员分数增加增量】(RedisSortedSetController-zincrby)-存储有序集合-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集合成员分数增加增量】(RedisSortedSetController-zincrby)-存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集合成员分数增加增量】(RedisSortedSetController-zincrby)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递增(从小到大)来排序。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrange",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(start == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "start为空");
		} else if(end == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "end为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrange(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(RedisSortedSetController-zrange)-指定区间内的成员-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrange)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(RedisSortedSetController-zrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递增(从小到大)来排序。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrangeWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrangeWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrangeWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(start == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "start为空");
		} else if(end == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "end为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrangeWithScores(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(RedisSortedSetController-zrangeWithScores)-指定区间内的成员-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrangeWithScores)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
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

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(RedisSortedSetController-zrangeWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据分数值范围查询存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrangeByScore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrangeByScore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(min == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "min为空");
		} else if(max == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "max为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrangeByScore(key, min, max);
			logger.info("===step2:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScore)-查询存储有序集合-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScore)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据分数值范围查询存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrangeByScoreWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrangeByScoreWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScoreWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(min == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "min为空");
		} else if(max == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "max为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrangeByScoreWithScores(key, min, max);
			logger.info("===step2:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScoreWithScores)-查询存储有序集合-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScoreWithScores)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
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

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【根据分数值范围查询存储有序集合】(RedisSortedSetController-zrangeByScoreWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrank",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrank(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集中成员排名】(RedisSortedSetController-zrank)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zrank(key, member);
			logger.info("===step2:【有序集中成员排名】(RedisSortedSetController-zrank)-成员排名-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集中成员排名】(RedisSortedSetController-zrank)-成员排名-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集中成员排名】(RedisSortedSetController-zrank)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrem",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrem(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除有序集key中的一个成员】(RedisSortedSetController-zrem)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zrem(key, member);
			logger.info("===step2:【移除有序集key中的一个成员】(RedisSortedSetController-zrem)-移除一个成员-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除有序集key中的一个成员】(RedisSortedSetController-zrem)-移除一个成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除有序集key中的一个成员】(RedisSortedSetController-zrem)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除有序集 key 中的多个成员，不存在的成员将被忽略。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/zrem",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zremArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除有序集key中的多个成员】(RedisSortedSetController-zremArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(members == null || members.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "members为空");
		}

		long len = 0l;
		try {
			len = redisService.zrem(key, members);
			logger.info("===step2:【移除有序集key中的多个成员】(RedisSortedSetController-zremArray)-移除多个成员-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除有序集key中的多个成员】(RedisSortedSetController-zremArray)-移除多个成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除有序集key中的多个成员】(RedisSortedSetController-zremArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zremrangeByRank",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zremrangeByRank(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除指定排名区间内的所有成员】(RedisSortedSetController-zremrangeByRank)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(start == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "start为空");
		} else if(end == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "end为空");
		}

		long len = 0l;
		try {
			len = redisService.zremrangeByRank(key, start, end);
			logger.info("===step2:【移除指定排名区间内的所有成员】(RedisSortedSetController-zremrangeByRank)-移除指定排名区间内的成员-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除指定排名区间内的所有成员】(RedisSortedSetController-zremrangeByRank)-移除指定排名区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除指定排名区间内的所有成员】(RedisSortedSetController-zremrangeByRank)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 分数值范围删除存储有序集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zremrangeByScore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zremrangeByScore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【分数值范围删除存储有序集合】(RedisSortedSetController-zremrangeByScore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(min == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "min为空");
		} else if(max == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "max为空");
		}

		long len = 0l;
		try {
			len = redisService.zremrangeByScore(key, min, max);
			logger.info("===step2:【分数值范围删除存储有序集合】(RedisSortedSetController-zremrangeByScore)-删除存储有序集合-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【分数值范围删除存储有序集合】(RedisSortedSetController-zrangeByScore)-删除存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【分数值范围删除存储有序集合】(RedisSortedSetController-zremrangeByScore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递减(从大到小)来排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrevrange",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrevrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(start == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "start为空");
		} else if(end == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "end为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrevrange(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrange)-指定区间内的成员-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrange)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递减(从大到小)来排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrevrangeWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrevrangeWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrangeWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(start == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "start为空");
		} else if(end == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "end为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrevrangeWithScores(key, start, end);
			logger.info("===step2:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrangeWithScores)-指定区间内的成员-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrangeWithScores)-指定区间内的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
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

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中指定区间内的成员】(RedisSortedSetController-zrevrangeWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrevrangeByScore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrevrangeByScore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(min == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "min为空");
		} else if(max == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "max为空");
		}

		Set<String> result = null;
		try {
			result = redisService.zrevrangeByScore(key, max, min);
			logger.info("===step2:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScore)-查询存储有序集合-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScore)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrevrangeByScoreWithScores",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrevrangeByScoreWithScores(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScoreWithScores)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Double min = req.getMin();
		Double max = req.getMax();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(min == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "min为空");
		} else if(max == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "max为空");
		}

		Set<Tuple> tuples = null;
		try {
			tuples = redisService.zrevrangeByScoreWithScores(key, max, min);
			logger.info("===step2:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScoreWithScores)-查询存储有序集合-返回信息, tuples:{}", tuples == null ? null : tuples.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScoreWithScores)-查询存储有序集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
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

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【有序集key中max和min之间所有的成员】(RedisSortedSetController-zrevrangeByScoreWithScores)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集key中成员member的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zrevrank",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zrevrank(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回有序集key中成员member的排名】(RedisSortedSetController-zrevrank)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.zrevrank(key, member);
			logger.info("===step2:【返回有序集key中成员member的排名】(RedisSortedSetController-zrevrank)-成员member排名-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【【返回有序集key中成员member的排名】(RedisSortedSetController-zrevrank)-成员member排名-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回有序集key中成员member的排名】(RedisSortedSetController-zrevrank)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回有序集key中，成员member的score 值。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/zscore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse zscore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【有序集key中成员member的score值】(RedisSortedSetController-zscore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		double len = 0d;
		try {
			len = redisService.zscore(key, member);
			logger.info("===step2:【有序集key中成员member的score值】(RedisSortedSetController-zscore)-成员score值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【有序集key中成员member的score值】(RedisSortedSetController-zscore)-成员score值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【有序集key中成员member的score值】(RedisSortedSetController-zscore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/************************** jredis SortedSet(有序集合) 命令工具方法 ****************************/

}