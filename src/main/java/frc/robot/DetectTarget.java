package frc.robot;

import com.kylecorry.frc.vision.Range;
import com.kylecorry.frc.vision.camera.CameraSettings;
import com.kylecorry.frc.vision.camera.FOV;
import com.kylecorry.frc.vision.camera.Resolution;
import com.kylecorry.frc.vision.contourFilters.ContourFilter;
import com.kylecorry.frc.vision.contourFilters.StandardContourFilter;
import com.kylecorry.frc.vision.filters.HSVFilter;
import com.kylecorry.frc.vision.filters.TargetFilter;
import com.kylecorry.frc.vision.targetConverters.TargetGrouping;
import com.kylecorry.frc.vision.targeting.Target;
import com.kylecorry.frc.vision.targeting.TargetFinder;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.net.URL;
import java.util.List;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//

public class DetectTarget {
 /**
 * Shoots at top target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
public Boolean shootTopTarget() {
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      //OpenCVManager.getInstance().load(new SystemProperties());
      URL imgURL = getClass().getResource("/fronttarget3.JPG"); // Change this to test other images
      Mat image = Imgcodecs.imread(imgURL.getFile());

    if (targetTopTarget(image)) {

    // Shoot the ball
    return true;
    }
    return false;
}
/**
 * Moves Robot to target top vision target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
public Boolean targetTopTarget(Mat image) {

    Target current = detectTopTarget(image);
        if (true/* Target Size too Big */) {
            /* move robot */
            }
        else if (true/* target size too small */) {

            }
            else  if (true/*target too far left */) {

            }
            else if (true/* target too far right */) {

            }
            else if (true/* target too right skewed */) {

            }
            else if (true/* target too left skewed */) {

            }
            else {
                return true; /*fire*/
            }
    return false;
}
 /**
 * Detects the 2020 top vision target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
public Target detectTopTarget(Mat image){
    // Adjust these parameters for your team's needs
    Target foundTarget;

    // Hue/Sat/Value filter parameters
    Range hsvHue = new Range(13, 132);
    Range hsvSaturation = new Range(162, 255);
    Range hsvValue = new Range(0, 255);
    
    // Contour filter parameters
    Range area = new Range(10, 100);
    Range fullness = new Range(0, 400);
    Range aspectRatio = new Range(200, 400);

     area = new Range(0, 10000);
     fullness = new Range(0, 10000);
     aspectRatio = new Range(0, 10000);
  
    // Camera settings
    FOV fov = new FOV(50, 40); // This is the approx. Microsoft LifeCam FOV
    Resolution resolution = new Resolution(720, 478);
    boolean cameraInverted = false;
  

    int imageArea = resolution.getArea();
  
    // An HSV filter may be better for FRC target detection
    CameraSettings cameraSettings = new CameraSettings(cameraInverted, fov, resolution);
    //TargetFilter targetFilter = new HSVFilter(new Range(50, 70), new Range(100, 255), new Range(100, 255));
    TargetFilter targetFilter = new HSVFilter(hsvHue, hsvSaturation, hsvValue);
    ContourFilter contourFilter = new StandardContourFilter( area, fullness, aspectRatio, imageArea);
    TargetGrouping targetGrouping = TargetGrouping.SINGLE;

    TargetFinder targetFinder = new TargetFinder(cameraSettings, targetFilter, contourFilter, targetGrouping);

    // Find targets
    List<Target> targets = targetFinder.findTargets(image);
    
    // If the current target is a left and the next is a right, make it a pair
    for (int i = 0; i < targets.size(); i++) {
        if (targets.get(i).getPercentArea() >= 1){
            if (targets.get(i).getVerticalAngle() <= -20){
                System.out.println("-->"+targets.get(i).toString());
                foundTarget = targets.get(i);
                SaveTargetImage("findTopTarget", foundTarget, image);
                //return targets.get(i);
            }
        }
    }

    return targets.get(0);
  }

  public void SaveTargetImage(String name, Target target, Mat image) {
    RotatedRect boundary = target.getBoundary();

    double height = boundary.boundingRect().height;
    double verticalPPI = height / 100;
    double holeYDist = 8.25;
    double centerY = boundary.center.y + holeYDist * verticalPPI;

    Imgproc.circle(image, new Point(boundary.center.x, centerY), boundary.boundingRect().width/2, new Scalar(255, 0, 255), 4);
    Imgproc.drawMarker(image, new Point(boundary.center.x, centerY), new Scalar(255, 0, 255), Core.TYPE_GENERAL, 30, 2);    

    // The following line is for desktop testing, use the CameraServer to display the image on a robot
    Imgcodecs.imwrite("snap_" + name + System.currentTimeMillis() + ".jpg", image);
  }
	public void CleanupOldFiles() {
		File dir = new File("/");
        String pattern = "snap(.*)";
        Pattern r = Pattern.compile(pattern);
        System.out.println("path: "+ dir.getAbsolutePath());

        // Now create matcher object.
		File[] listFiles = dir.listFiles();
		for(File file : listFiles){
            Matcher m = r.matcher(file.getAbsolutePath());
            if (m.find()) {
                System.out.println("delete: "+ file.getAbsolutePath());
//                file.delete(); //
            }
		}
	}

}