package com.free.forum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author swan
 */
@RestController
@RequestMapping("/check")
public class CheckController {

    @GetMapping("/check")
    public boolean check(@RequestParam("check") String check, HttpSession session) {
        String realCheck = (String) session.getAttribute("CHECKCODE_SERVER");
        return check.equalsIgnoreCase(realCheck);
    }

    @GetMapping("/code")
    public void generateCode(HttpServletResponse response, HttpSession session) throws IOException {
        // 设置响应头，禁止浏览器缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");

        // 配置图片尺寸
        int width = 80;
        int height = 30;
        // 创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 创建画笔对象，用于绘制图像
        Graphics2D g = image.createGraphics();

        // 填充背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 生成随机验证码
        String checkCode = generateRandomCode();
        // 将验证码存储在会话对象中
        session.setAttribute("CHECKCODE_SERVER", checkCode);

        // 设置画笔颜色和字体样式
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        // 在图片上绘制验证码
        g.drawString(checkCode, 15, 25);

        // 释放画笔对象和图像对象
        g.dispose();

        // 将图像对象转换为字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);

        // 设置响应头，指定返回图片数据的类型为PNG
        response.setContentType("image/png");

        // 将图片字节数组写入响应输出流，并刷新和关闭输出流
        response.getOutputStream().write(bos.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private String generateRandomCode() {
        String base = "0123456789ABCDEFGabcdefg";
        int size = base.length();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(size);
            char c = base.charAt(index);
            sb.append(c);
        }
        return sb.toString();
    }
}