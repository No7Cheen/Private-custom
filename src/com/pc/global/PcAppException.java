package com.pc.global;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.pc.app.PcBaseApplicationImpl;
import com.pc.utils.StringUtils;
import com.privatecustom.publiclibs.R;

/**
 * 公共异常类.
 */
public class PcAppException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 异常消息 */
	private String msg = null;

	/**
	 * 构造异常类
	 * @param e 异常
	 */
	public PcAppException(Exception e) {
		super();

		try {
			if (e instanceof HttpHostConnectException) {
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.unknownhostexception);
				msg = e.getMessage();
			} else if (e instanceof ConnectException) {
				msg = e.getMessage();
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.connectexception);
			} else if (e instanceof UnknownHostException) {
				msg = e.getMessage();
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.unknownhostexception);
			} else if (e instanceof SocketException) {
				msg = e.getMessage();
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.socketexception);
			} else if (e instanceof SocketTimeoutException) {
				msg = e.getMessage();
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.sockettimeoutexception);
			} else if (e instanceof NullPointerException) {
				msg = e.getMessage();
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.nullpointerexception);
			} else if (e instanceof ClientProtocolException) {
				msg = e.getMessage();
				// msg = PcBaseApplicationImpl.getContext().getString(R.string.clientprotocolexception);
			} else {
				if (e == null || StringUtils.isNull(e.getMessage())) {
					msg = PcBaseApplicationImpl.getContext().getString(R.string.nullmessageexception);
				} else {
					msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}

	}

	/**
	 * 用一个消息构造异常类
	 * 
	 * @param message 异常的消息
	 */
	public PcAppException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 描述：获取异常信息
	 * 
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
