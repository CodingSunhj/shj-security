package com.shj.security.core.validate.code.image;

import com.shj.security.core.properties.SecurityProperties;
import com.shj.security.core.validate.code.ValidateCodeGenerator;
import com.shj.security.core.validate.code.image.ImageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageCodeGenerator implements ValidateCodeGenerator {
    @Autowired
    SecurityProperties securityProperties;
    @Override
    public ImageCode generate(ServletRequest request) {

        int width = ServletRequestUtils.getIntParameter(request, "width", securityProperties.getCode().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(request, "height", securityProperties.getCode().getImage().getHeight());
        Random random = new Random();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);
        //随机数生成
        StringBuffer sb = new StringBuffer();
        int number = 0;
        while (number<securityProperties.getCode().getImage().getLength()){
            int t = random.nextInt(123);
            if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57))) {
                sb.append((char) t);
                number++;
            }
        }
        //干扰线生成
        int x = random.nextInt(4), y = 0;
        int x1 = width - random.nextInt(4), y1 = 0;
        for (int i = 0; i < 1; i++) {
            g.setColor(Color.YELLOW);
            y = random.nextInt(height - random.nextInt(4));
            y1 = random.nextInt(height - random.nextInt(4));
            g.drawLine(x, y, x1, y1);
        }

        int fsize=(int)(height*0.8);//字体大小为图片高度的80%
        int fx=0;
        int fy=fsize;
        g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,fsize));
        //写字符
        for(int i=0;i<sb.toString().length();i++) {
            fy = (int) ((Math.random() * 0.3 + 0.6) * height);//每个字符高低是否随机
            g.setColor(Color.RED);
            g.drawString(sb.toString().charAt(i) + "", fx, fy);
            fx += (width / sb.toString().length()) * (Math.random() * 0.3 + 0.8); //依据宽度浮动
        }
        // 添加噪点
        float yawpRate = 0.05f;
        int area = (int) (yawpRate * width * height);//噪点数量
        for (int i = 0; i < area; i++) {
            int xxx = random.nextInt(width);
            int yyy = random.nextInt(height);
            int rgb = Color.WHITE.getRGB();
            image.setRGB(xxx, yyy, rgb);
        }
        g.dispose();
        return new ImageCode(image,sb.toString(),securityProperties.getCode().getImage().getExpireIn());
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
