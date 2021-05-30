package kr.or.dining_together.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.model.SingleResult;

@Service
public class ResponseService {

	// 단일건 결과 처리
	public <T> SingleResult<T> getSingleResult(T data) {
		SingleResult<T> result = new SingleResult<>();
		result.setData(data);
		setSuccessResult(result);
		return result;
	}

	// 다중건 결과 처리
	public <T> ListResult<T> getListResult(List<T> list) {
		ListResult<T> result = new ListResult<>();
		result.setList(list);
		setSuccessResult(result);
		return result;
	}

	// 성공 결과만 처리
	public CommonResult getSuccessResult() {
		CommonResult result = new CommonResult();
		setSuccessResult(result);
		return result;
	}

	// 성공하면 api 성공 데이터 세팅
	private void setSuccessResult(CommonResult result) {
		result.setSuccess(true);
		result.setCode(CommonResponse.SUCCESS.getCode());
		result.setMsg(CommonResponse.SUCCESS.getMsg());
	}

	// 실패 코드와, msg받아 실패결과 처리
	public CommonResult getFailResult(int code, String msg) {
		CommonResult result = new CommonResult();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	// 실패 결과만 처리
	public CommonResult getDefaultFailResult() {
		CommonResult result = new CommonResult();
		setFailResult(result);
		return result;
	}

	// 실패하면 api 실패 데이터 세팅
	private void setFailResult(CommonResult result) {
		result.setSuccess(false);
		result.setCode(CommonResponse.FAIL.getCode());
		result.setMsg(CommonResponse.FAIL.getMsg());
	}

	public enum CommonResponse {
		SUCCESS(0, "성공"),
		FAIL(-1, "실패");

		int code;
		String msg;

		CommonResponse(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public int getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}

}
