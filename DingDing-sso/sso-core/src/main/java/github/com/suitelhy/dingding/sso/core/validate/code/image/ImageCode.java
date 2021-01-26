package github.com.suitelhy.dingding.sso.core.validate.code.image;

import github.com.suitelhy.dingding.sso.core.validate.code.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;


/**
 * 图片验证码
 *
 * @author zhailiang
 *
 */
public class ImageCode
		extends ValidateCode {
	
	private static final long serialVersionUID = -6020470039852318468L;
	
	private BufferedImage image; 
	
	public ImageCode(BufferedImage image, String code, int expireIn){
		super(code, expireIn);
		this.image = image;
	}
	
	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
		super(code, expireTime);
		this.image = image;
	}

	//===== Getter and Setter =====//

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
