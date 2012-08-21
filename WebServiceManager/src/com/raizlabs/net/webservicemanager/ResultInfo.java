package com.raizlabs.net.webservicemanager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.raizlabs.net.HttpUtils;

import android.util.Log;

/**
 * Object which contains information about the result of a request.
 * @author Dylan James
 *
 * @param <ResultType> The type of the result which will be returned
 */
public class ResultInfo<ResultType> {
	Date RequestDate;
	public Date getRequestDate() { return RequestDate; }
	ResultType Result;
	public ResultType getResult() { return Result; }
	
	Integer ResponseCode;
	public Integer getResponseCode() { return ResponseCode; }
	String ResponseMessage;
	public String getResponseMessage() { return ResponseMessage; }
	
	public boolean isStatusOK() { return ResponseCode != null && HttpUtils.isResponseOK(ResponseCode); }
	
	boolean cancelled;
	/**
	 * @return True if the request was cancelled.
	 */
	public boolean wasCancelled() { return cancelled; }
	void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	/**
	 * Creates a {@link ResultInfo} by wrapping the given result and
	 * {@link HttpURLConnection}.
	 * @param result The result of the request.
	 * @param requestDate The {@link Date} the request was completed.
	 * @param connection The {@link HttpURLConnection} resulting from the request.
	 */
	public ResultInfo(ResultType result, Date requestDate, HttpURLConnection connection) {
		this(result, requestDate);
		wrap(connection);
	}
	
	/**
	 * Creates a {@link ResultInfo} by wrapping the given result and
	 * {@link HttpResponse}.
	 * @param result The result of the request.
	 * @param requestDate The {@link Date} the request was completed.
	 * @param response The {@link HttpResponse} resulting from the request.
	 */
	public ResultInfo(ResultType result, Date requestDate, HttpResponse response) {
		this(result, requestDate);
		wrap(response);
	}
	
	
	private ResultInfo(ResultType result, Date requestDate) {
		this.Result = result;
		this.RequestDate = requestDate;
		this.cancelled = false;
	}
	
	
	private void wrap(HttpURLConnection conn) {
		if (conn != null) {
			try {
				this.ResponseCode = conn.getResponseCode();
				this.ResponseMessage = conn.getResponseMessage();
			} catch (IOException e) {
				Log.w(getClass().getName(), "IO Exception when wrapping URLConnection: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private void wrap(HttpResponse response) {
		if (response != null && response.getStatusLine() != null) {
			StatusLine status = response.getStatusLine();
			if (status != null) {
				this.ResponseCode = status.getStatusCode();
				this.ResponseMessage = status.getReasonPhrase();
			}
		}
	}
}
