package frc.robot;

import frc.robot.DetectTarget;
import com.kylecorry.frc.vision.Range;
import com.kylecorry.frc.vision.camera.CameraSettings;
import com.kylecorry.frc.vision.camera.FOV;
import com.kylecorry.frc.vision.camera.Resolution;
import com.kylecorry.frc.vision.contourFilters.ContourFilter;
import com.kylecorry.frc.vision.contourFilters.StandardContourFilter;
import com.kylecorry.frc.vision.filters.HSVFilter;
import com.kylecorry.frc.vision.filters.TargetFilter;
import com.kylecorry.frc.vision.targetConverters.TargetGrouping;
import com.kylecorry.frc.vision.targetConverters.TargetUtils;
import com.kylecorry.frc.vision.targeting.Target;
import com.kylecorry.frc.vision.targeting.TargetFinder;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class testDetectTarget {  
  @Test
  public void test(){
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      
      DetectTarget detectTarget = new DetectTarget();
      detectTarget.CleanupOldFiles();
 
      OpenCVManager.getInstance().load(new SystemProperties());
      System.out.println(getClass().getResource("").getFile());
      //System.out.println(Imgcodecs.haveImageReader​(getClass().getResource("frontview2019.png").getFile()));
      
      //Mat image = Imgcodecs.imread(getClass().getResource("frontview2019.png").getFile());
      Mat image = Imgcodecs.imread("fronttarget4.jpg");
        System.out.println("image width:"+image.width());
        System.out.println("image height:"+image.height());

      System.out.println("image width:"+image.toString());
      Target target = detectTarget.detectTopTarget(image);

        Assert.assertEquals(true,(target!=null));

        detectTarget.SaveTargetImage("findTopTarget", target, image);

        Assert.assertEquals(37,(int)target.getHorizontalAngle());
        Assert.assertEquals(-31,(int)target.getVerticalAngle());
        Assert.assertEquals(68,(int)target.getPercentArea());
      }


}