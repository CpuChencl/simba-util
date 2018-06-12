package com.simba.framework.util.code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * 条形码工具类
 * 
 * @author caozj
 *
 */
public class BarCodeUtil {

  private static final Log logger = LogFactory.getLog(BarCodeUtil.class);

  /**
   * 将条形码图片写到输出流中
   * 
   * @param out
   * @param code
   * @throws IOException
   */
  public static void writeToStream(OutputStream out, String code) throws IOException {
    Code128Bean bean = new Code128Bean();
    final int dpi = 150;
    // barcode
    bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
    bean.setHeight(7.6);
    bean.doQuietZone(true);
    bean.setQuietZone(0);// 两边空白区
    // human-readable
    bean.setFontName("Helvetica");
    bean.setFontSize(3);
    bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
    BitmapCanvasProvider canvas =
        new BitmapCanvasProvider(out, "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
    bean.generateBarcode(canvas, code);
    canvas.finish();
  }

  /**
   * 将条形码图片写到文件中
   * 
   * @param file
   * @param code
   */
  public static void writeToFile(File file, String code) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      writeToStream(out, code);
    } catch (Exception e) {
      logger.error("生成条形码异常", e);
    } finally {
      IOUtils.closeQuietly(out);
    }
  }
}
