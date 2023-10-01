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
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");

        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        String checkCode = generateRandomCode();
        session.setAttribute("CHECKCODE_SERVER", checkCode);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(checkCode, 15, 25);

        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);

        response.setContentType("image/png");
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