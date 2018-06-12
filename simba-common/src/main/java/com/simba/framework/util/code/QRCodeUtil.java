package com.simba.framework.util.code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.simba.model.constant.ConstantData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码工具类
 * 
 * @author caozj
 *
 */
public class QRCodeUtil {

  public static final int WIDTH = 400;
  public static final int HEIGHT = 400;
  public static final String FORMAT = "gif";
  private static final int BLACK = 0xFF000000;
  private static final int WHITE = 0xFFFFFFFF;

  /**
   * 生成二维码图片
   * 
   * @param matrix
   * @return
   */
  public static BufferedImage toBufferedImage(BitMatrix matrix) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
      }
    }
    return image;
  }

  /**
   * 将二维码图片写到文件中
   * 
   * @param matrix
   * @param format
   * @param file
   * @throws IOException
   */
  public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, file)) {
      throw new IOException("Could not write an image of format " + format + " to " + file);
    }
  }

  /**
   * 将二维码图片写到输出流中
   * 
   * @param matrix
   * @param format
   * @param stream
   * @throws IOException
   */
  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
      throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }

  /**
   * 构造生成二维码的对象
   * 
   * @param text
   * @param format
   * @param height
   * @param width
   * @return
   * @throws WriterException
   */
  public static BitMatrix buildBitMatrix(String text, String format, int height, int width)
      throws WriterException {
    Map<EncodeHintType, String> param = new HashMap<EncodeHintType, String>();
    // 内容所使用编码
    param.put(EncodeHintType.CHARACTER_SET, ConstantData.DEFAULT_CHARSET);
    BitMatrix bitMatrix =
        new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, param);
    return bitMatrix;
  }

  /**
   * 构造生成二维码的对象
   * 
   * @param text
   * @param height
   * @param width
   * @return
   * @throws WriterException
   */
  public static BitMatrix buildBitMatrix(String text, int height, int width)
      throws WriterException {
    return buildBitMatrix(text, FORMAT, height, width);
  }

  /**
   * 构造生成二维码的对象
   * 
   * @param text
   * @return
   * @throws WriterException
   */
  public static BitMatrix buildBitMatrix(String text) throws WriterException {
    return buildBitMatrix(text, FORMAT, HEIGHT, WIDTH);
  }

  /**
   * 将二维码图片写到输出流中
   * 
   * @param text
   * @param stream
   * @throws WriterException
   * @throws IOExceptio
   */
  public static void writeToStream(String text, OutputStream stream)
      throws IOException, WriterException {
    writeToStream(buildBitMatrix(text), FORMAT, stream);
  }

  /**
   * 将二维码图片写到输出流中
   * 
   * @param text
   * @param height
   * @param width
   * @param stream
   * @throws IOException
   * @throws WriterException
   */
  public static void writeToStream(String text, int height, int width, OutputStream stream)
      throws IOException, WriterException {
    writeToStream(buildBitMatrix(text, height, width), FORMAT, stream);
  }

  /**
   * 将二维码图片写到文件中
   * 
   * @param text
   * @param file
   * @throws WriterException
   * @throws IOException
   */
  public static void writeToFile(String text, File file) throws IOException, WriterException {
    writeToFile(buildBitMatrix(text), FORMAT, file);
  }

  /**
   * 将二维码图片写到文件中
   * 
   * @param text
   * @param height
   * @param width
   * @param file
   * @throws IOException
   * @throws WriterException
   */
  public static void writeToFile(String text, int height, int width, File file)
      throws IOException, WriterException {
    writeToFile(buildBitMatrix(text, height, width), FORMAT, file);
  }
}
