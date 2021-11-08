package ee.ut;

import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("user.dir", System.getProperty("user.dir")+"/src/test");
        new App().start(stage);
    }

    @Test
    public void testStart() throws InterruptedException, IOException {
        FxRobot robot = new FxRobot();
        //Start new timeline
        Thread.sleep(100);
        robot.clickOn("#startNewTimeline");
        Thread.sleep(100);
        //Change settings and save
        robot.clickOn("#labelType");
        robot.clickOn("Punkt");
        robot.doubleClickOn("#eventSpace");
        robot.write("300");
        robot.clickOn("#eventsPacked");
        robot.clickOn("#saveButton1");
        //Add new event
        robot.clickOn("Sündmused");
        robot.clickOn("#newEventButton");
        robot.clickOn("#htmlEditor");
        robot.eraseText(16);
        robot.write("ONE");
        robot.clickOn("#saveButton");
        //Add another event
        robot.clickOn("#newEventButton");
        robot.clickOn("#htmlEditor");
        robot.eraseText(16);
        robot.write("TWO");
        robot.clickOn("#saveButton");
        //Edit first event
        robot.clickOn("Muuda");
        robot.clickOn("#htmlEditor");
        robot.eraseText(3);
        robot.write("1");
        robot.clickOn("#saveButton");
        //Open preview
        robot.clickOn("eelvaade");
        robot.clickOn("kuva eelvaade");
        robot.closeCurrentWindow(); //close opened window > if preview wasn't opened, error with next line
        //Save timeline in a file
        robot.clickOn("file");
        robot.clickOn("Salvesta ajajoon");
        Thread.sleep(100);
        //Check saved data
        String savedData = Files.readString(Path.of(System.getProperty("user.dir") + "\\result\\data.js"));
        Assert.assertTrue(savedData.contains("DOT")); //labelType is "PUNKT"
        Assert.assertTrue(savedData.contains("300")); //eventSpace is "300"
        Assert.assertTrue(savedData.contains("\"packed\":true")); //eventsPacked is checked
        Assert.assertTrue(savedData.contains("TWO")); //event with text "TWO"
        Assert.assertFalse(savedData.contains("ONE")); //event with text "ONE" was edited
        Assert.assertTrue(savedData.contains("1")); //event with text "ONE" was edited to "1"
    }

    @Override
    public void stop(){
        deleteDirectory(Paths.get(System.getProperty("user.dir")+"/result").toFile());
        System.out.println(System.getProperty("user.dir"));
        System.setProperty("user.dir", System.getProperty("user.dir").replace("/src/test",""));
        System.out.println(System.getProperty("user.dir"));
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }


}