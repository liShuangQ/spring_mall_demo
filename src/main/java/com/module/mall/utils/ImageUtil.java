package com.module.mall.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.IOException;

/**
 * 描述：     图片工具类
 */
public class ImageUtil {

    public static void main(String[] args) throws IOException {
        String path = "/Users/lishuangqi/myselfwork/mall-file/";
        String fileName = "a2f6ad50-b3fa-4c73-b403-7d46e8bfd663.png";
        //切割
        Thumbnails.of(path + fileName).sourceRegion(Positions.BOTTOM_RIGHT, 200, 200).size(200, 200).toFile(path + "crop.jpg");

        //缩放
        Thumbnails.of(path + fileName).scale(0.7).toFile(path + "scale1.jpg");
        Thumbnails.of(path + fileName).scale(1.5).toFile(path + "scale2.jpg");
        Thumbnails.of(path + fileName).size(500, 500).keepAspectRatio(false).toFile(path + "size1.jpg");
        Thumbnails.of(path + fileName).size(500, 500).keepAspectRatio(true).toFile(path + "size2.jpg");

        //旋转
        Thumbnails.of(path + fileName).size(500, 500).keepAspectRatio(true).rotate(90).toFile(path + "rotate1.jpg");
        Thumbnails.of(path + fileName).size(500, 500).keepAspectRatio(true).rotate(180).toFile(path + "rotate2.jpg");

    }


}
