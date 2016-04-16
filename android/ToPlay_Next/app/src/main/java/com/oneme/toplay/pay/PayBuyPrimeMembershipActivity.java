package com.oneme.toplay.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.ParseUser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class PayBuyPrimeMembershipActivity extends BaseActivity {

	//商户PID
	public static final String PARTNER     = "2088811640213787";
	//商户收款账号
	public static final String SELLER      = "toplayiosandroid@gmail.com";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE= "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMmKAPipLCOzZ3kF\n" +
			"q4yod+TFxsmg2A8t+OXDUfejOI5sxY3wSD5HS5/mX81FJ4wnAajHAQQC+rL2t5Zi\n" +
			"DVK1cspMxf3JgIhXccbsebZeLcO6S6+ekq8zF10Lm2sM4XoSjTlBY+/vhsopC5Ir\n" +
			"Y4Lc5uj7lXjg1l7DotO37wuLtnaxAgMBAAECgYAOF/d/bB0pFferb+kSOgnAVtBS\n" +
			"80uIrDdWofUOczyWQScDiQUUTdoNAYg7i5V7aJLmIZyBkk/DyhsGii7SVNwFB9oC\n" +
			"3Py7M6F99wjMu08PCcwO+Bl34lfqJ3xzj5BO/JawSQ8iBzswAFHpsryMyFGRI+MO\n" +
			"073t/XjpdwG02yjfEQJBAOXnH2AKZyY5+qP9gDKYD9WkGlGoYXCXWRNFpVSUWwgt\n" +
			"6zOZPi4izfEXRtxcspwBxH5VsMOM0ium44iBU9lbHbUCQQDgaqSZL/Xv/gHEK1dm\n" +
			"amSU/q4JeC9+su5GR6MyznWD8FIlSO4P+LlFiF/UEWXLWVvR3SbABFr4ArNcGxw/\n" +
			"6vKNAkA9zb4fgWxtWFTK1REriLlpA26X2R4ouGcXyTdC34cBUebSvjV7DZwzXcCM\n" +
			"6NaTHd6G395fE3JEjd7JCYtS+S2pAkAm6dcIyQw4RS9BONK664/tVgwPkEuMCLLQ\n" +
			"dPOTh8FQ04cq9+5vzo0xxWf4edvQ2vnU9igV+6taff1j5z+Kxc6xAkAMQPKRDSi2\n" +
			"/zPrOwhTF1DyZO7v90AYo0jZJ176eCcCYieJIwE9rAVLCq8l+H1r7dFr9kxh3WEi\n" +
			"I9CTaMAmIjhm";
	//支付宝公钥
	public static final String           RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//playround公钥
	public static final String PLAYROUND_RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJigD4qSwjs2d5BauMqHfkxcbJ\n" +
			"oNgPLfjlw1H3oziObMWN8Eg+R0uf5l/NRSeMJwGoxwEEAvqy9reWYg1StXLKTMX9\n" +
			"yYCIV3HG7Hm2Xi3DukuvnpKvMxddC5trDOF6Eo05QWPv74bKKQuSK2OC3Obo+5V4\n" +
			"4NZew6LTt+8Li7Z2sQIDAQAB";


	private ParseUser muser = ParseUser.getCurrentUser();

	private static String morderTradeNo = null;

	private TextView mpayprimemembershipvenue = null;
	private TextView mpayfee = null;
	private TextView mphone  = null;

	String mname;
	String mnameid;
	String mcurrencysymbol;
	String mcardname;
	String mcardprice;
	String mphonenumber;



	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayBuyPrimeMembershipActivity.this, getResources().getString(R.string.OMEPARSEPAYSUCCESS),
							Toast.LENGTH_SHORT).show();
					Intent newIntent = new Intent();
					newIntent.putExtra(Application.INTENT_EXTRA_PRIMEPAYNO, morderTradeNo);
					setResult(AppCompatActivity.RESULT_OK, newIntent);
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayBuyPrimeMembershipActivity.this, getResources().getString(R.string.OMEPARSEPAYINPROGRESS),
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayBuyPrimeMembershipActivity.this, getResources().getString(R.string.OMEPARSEPAYFAIL),
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayBuyPrimeMembershipActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ome_activity_buy_prime_membership_pay);

		Toolbar toolbar = getActionBarToolbar();
		toolbar.setNavigationIcon(R.drawable.ic_up);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				//navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(PayBookingVenueActivity.this,
				//		BookingActivity.class)));
			}
		});

		Bundle extras     = getIntent().getExtras();

		if (extras != null) {
			mname           = extras.getString(Application.INTENT_EXTRA_VENUEJSONNAME);
			mnameid         = extras.getString(Application.INTENT_EXTRA_VENUEJSONNAMEID);
			mcurrencysymbol = extras.getString(Application.INTENT_EXTRA_VENUEJSONCURRENCY);
			mcardname       = extras.getString(Application.INTENT_EXTRA_VENUEJSONCARDNAME);
			mcardprice      = extras.getString(Application.INTENT_EXTRA_VENUEJSONCARDPRICE);
			mphonenumber    = extras.getString(Application.INTENT_EXTRA_USERPHONE);
		}

		// set fee
		TextView currency   = (TextView) findViewById(R.id.pay_prime_membership_currency);
		currency.setText(mcurrencysymbol);

		mpayprimemembershipvenue = (TextView)findViewById(R.id.pay_prime_membership_venue);
		mpayprimemembershipvenue.setText(mname + " " + mcardname);

		mpayfee   = (TextView)findViewById(R.id.pay_prime_membership_fee);
		mpayfee.setText(mcardprice);

		mphone    = (TextView)findViewById(R.id.pay_prime_membership_phone_number);
		mphone.setText(mphonenumber);

	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		// 订单
		String orderInfo = getOrderInfo(mname + " " + mcardname, getResources().getString(R.string.OMEPARSEPAYBUYPRIMEMEMBERSHIPDESCRIPTION), mcardprice);

		// 对订单做RSA 签名
		String signature = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			signature = URLEncoder.encode(signature, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + signature + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayBuyPrimeMembershipActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayBuyPrimeMembershipActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what    = SDK_CHECK_FLAG;
				msg.obj     = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version  = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// record the trade no
		morderTradeNo = getOutTradeNo();

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + morderTradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		//orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 19);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
