package Resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class Images {


    public static BufferedImage titleScreenBackground;
    public static BufferedImage pauseBackground;
    public static BufferedImage selectionBackground;
    public static BufferedImage galagaCopyright;
    public static BufferedImage galagaSelect;
    public static BufferedImage muteIcon;
    public static BufferedImage galagaPlayerLaser;
    public static BufferedImage[] startGameButton;
    public static BufferedImage[] galagaLogo;
    public static BufferedImage[] pauseResumeButton;
    public static BufferedImage[] pauseToTitleButton;
    public static BufferedImage[] pauseOptionsButton;
    public static BufferedImage[] galagaPlayer;
    public static BufferedImage[] galagaPlayerDeath;
    public static BufferedImage[] galagaEnemyDeath;
    public static BufferedImage[] galagaEnemyBee;

    public static BufferedImage map1;
    public static BufferedImage map2;
    public static BufferedImage[] officialPacLogo;
    public static BufferedImage ghost;
    public static BufferedImage[] pacmanDots;
    public static BufferedImage[] pacmanRight;
    public static BufferedImage[] pacmanLeft;
    public static BufferedImage[] pacmanUp;
    public static BufferedImage[] pacmanDown;
    public static BufferedImage[] blinkyRight;
    public static BufferedImage[] blinkyLeft;
    public static BufferedImage[] blinkyUp;
    public static BufferedImage[] blinkyDown;
    public static BufferedImage[] pinkyRight;
    public static BufferedImage[] pinkyLeft;
    public static BufferedImage[] pinkyUp;
    public static BufferedImage[] pinkyDown;
    public static BufferedImage[] inkyRight;
    public static BufferedImage[] inkyLeft;
    public static BufferedImage[] inkyUp;
    public static BufferedImage[] inkyDown;
    public static BufferedImage[] clydeRight;
    public static BufferedImage[] clydeLeft;
    public static BufferedImage[] clydeUp;
    public static BufferedImage[] clydeDown;
    public static BufferedImage[] bound;
    public static BufferedImage intro;
    public static BufferedImage start;
    // fruit sprites
    public static BufferedImage[] pacmanFruits;



    public static BufferedImage galagaImageSheet;
    public SpriteSheet galagaSpriteSheet;

    public static BufferedImage pacmanImageSheet;
    public SpriteSheet pacmanSpriteSheet;

    public Images() {

        startGameButton = new BufferedImage[3];
        pauseResumeButton = new BufferedImage[2];
        pauseToTitleButton = new BufferedImage[2];
        pauseOptionsButton = new BufferedImage[2];
        galagaLogo = new BufferedImage[3];
        galagaPlayer = new BufferedImage[8];//not full yet, only has second to last image on sprite sheet
        galagaPlayerDeath = new BufferedImage[8];
        galagaEnemyDeath = new BufferedImage[5];
        galagaEnemyBee = new BufferedImage[8];

        pacmanDots = new BufferedImage[3];
        pacmanFruits = new BufferedImage[5];
        pacmanRight = new BufferedImage[2];
        pacmanLeft = new BufferedImage[2];
        pacmanUp = new BufferedImage[2];
        pacmanDown = new BufferedImage[2];
        blinkyRight = new BufferedImage[2];
        blinkyLeft = new BufferedImage[2];
        blinkyUp = new BufferedImage[2];
        blinkyDown = new BufferedImage[2];
        pinkyRight = new BufferedImage[2];
        pinkyLeft = new BufferedImage[2];
        pinkyUp = new BufferedImage[2];
        pinkyDown = new BufferedImage[2];
        inkyRight = new BufferedImage[2];
        inkyLeft = new BufferedImage[2];
        inkyUp = new BufferedImage[2];
        inkyDown = new BufferedImage[2];
        clydeRight = new BufferedImage[2];
        clydeLeft = new BufferedImage[2];
        clydeUp = new BufferedImage[2];
        clydeDown = new BufferedImage[2];
        bound = new BufferedImage[16];
        officialPacLogo = new BufferedImage[3];


        try {

            startGameButton[0]= ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Start/NormalStartButton.png"));
            startGameButton[1]= ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Start/HoverStartButton.png"));
            startGameButton[2]= ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Start/ClickedStartButton.png"));

            titleScreenBackground = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/Title.png"));

            pauseBackground = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/Pause.png"));

            selectionBackground = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/Selection.png"));

            galagaCopyright = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/Copyright.png"));

            galagaSelect = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/galaga_select.png"));

            muteIcon = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/mute.png"));

            galagaLogo[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/galaga_logo.png"));
            galagaLogo[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Selection/Galaga/hover_galaga_logo.png"));
            galagaLogo[2] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Selection/Galaga/pressed_galaga_logo.png"));
            
            officialPacLogo[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/Pac-Man_logo.png"));
            officialPacLogo[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/Pac-Man_logo1.png"));
            officialPacLogo[2] = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/Pac-Man_logo2.png"));

            pauseResumeButton[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/Resume/NormalHoverResume.png"));
            pauseResumeButton[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/Resume/PressedResume.png"));

            pauseToTitleButton[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToTitle/NormalHoverToTitleButton.png"));
            pauseToTitleButton[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToTitle/PressedToTitleButton.png"));

            pauseOptionsButton[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToOptions/NormalHoverToOptionsButton.png"));
            pauseOptionsButton[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToOptions/PressedToOptionsButton.png"));

            galagaImageSheet = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/Galaga/Galaga.png"));
            galagaSpriteSheet = new SpriteSheet(galagaImageSheet);

            galagaPlayer[0] = galagaSpriteSheet.crop(160,55,15,16);

            galagaPlayerDeath[0] = galagaSpriteSheet.crop(209,48,32,32);
            galagaPlayerDeath[1] = galagaSpriteSheet.crop(209,48,32,32);
            galagaPlayerDeath[2] = galagaSpriteSheet.crop(247,48,32,32);
            galagaPlayerDeath[3] = galagaSpriteSheet.crop(247,48,32,32);
            galagaPlayerDeath[4] = galagaSpriteSheet.crop(288,47,32,32);
            galagaPlayerDeath[5] = galagaSpriteSheet.crop(288,47,32,32);
            galagaPlayerDeath[6] = galagaSpriteSheet.crop(327,47,32,32);
            galagaPlayerDeath[7] = galagaSpriteSheet.crop(327,47,32,32);

            galagaEnemyDeath[0] = galagaSpriteSheet.crop(201,191,32,32);
            galagaEnemyDeath[1] = galagaSpriteSheet.crop(223,191,32,32);
            galagaEnemyDeath[2] = galagaSpriteSheet.crop(247,191,32,32);
            galagaEnemyDeath[3] = galagaSpriteSheet.crop(280,191,32,32);
            galagaEnemyDeath[4] = galagaSpriteSheet.crop(320,191,32,32);

            galagaEnemyBee[0] = galagaSpriteSheet.crop(188,178,9,10);
            galagaEnemyBee[1] = galagaSpriteSheet.crop(162,178,13,10);
            galagaEnemyBee[2] = galagaSpriteSheet.crop(139,177,11,12);
            galagaEnemyBee[3] = galagaSpriteSheet.crop(113,176,14,13);
            galagaEnemyBee[4] = galagaSpriteSheet.crop(90,177,13,13);
            galagaEnemyBee[5] = galagaSpriteSheet.crop(65,176,13,14);
            galagaEnemyBee[6] = galagaSpriteSheet.crop(42,178,12,11);
            galagaEnemyBee[7] = galagaSpriteSheet.crop(19,177,10,13);


            galagaPlayerLaser = galagaSpriteSheet.crop(365 ,219,3,8);

            pacmanImageSheet = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/PacMan/Background.png"));
            pacmanSpriteSheet = new SpriteSheet(pacmanImageSheet);
            map1 = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/PacManMaps/map1.png"));
            map2 = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/PacManMaps/map2.png"));
            ghost = pacmanSpriteSheet.crop(456,64,16,16);
            pacmanDots[0] = pacmanSpriteSheet.crop(643,18,16,16);
            pacmanDots[1] = pacmanSpriteSheet.crop(623,18,16,16);
            pacmanDots[2] = pacmanSpriteSheet.crop(663, 18, 16, 16);
            
            //Fruit sprites
            pacmanFruits[0] = pacmanSpriteSheet.crop(490,49,13,14);
            pacmanFruits[1] = pacmanSpriteSheet.crop(505,49,13,14);
            pacmanFruits[2] = pacmanSpriteSheet.crop(521,49,14,14);
            pacmanFruits[3] = pacmanSpriteSheet.crop(537,49,14,14);
            pacmanFruits[4] = pacmanSpriteSheet.crop(554,49,13,15);

            bound[0] = pacmanSpriteSheet.crop(603,18,16,16);//single
            bound[1] = pacmanSpriteSheet.crop(615,37,16,16);//right open
            bound[2] = pacmanSpriteSheet.crop(635,37,16,16);//down open
            bound[3] = pacmanSpriteSheet.crop(655,37,16,16);//left open
            bound[4] = pacmanSpriteSheet.crop(655,57,16,16);//up open
            bound[5] = pacmanSpriteSheet.crop(655,75,16,16);//up/down
            bound[6] = pacmanSpriteSheet.crop(656,116,16,16);//left/Right
            bound[7] = pacmanSpriteSheet.crop(656,136,16,16);//up/Right
            bound[8] = pacmanSpriteSheet.crop(655,174,16,16);//up/left
            bound[9] = pacmanSpriteSheet.crop(655,155,16,16);//down/Right
            bound[10] = pacmanSpriteSheet.crop(655,192,16,16);//down/left
            bound[11] = pacmanSpriteSheet.crop(664,232,16,16);//all
            bound[12] = pacmanSpriteSheet.crop(479,191,16,16);//left
            bound[13] = pacmanSpriteSheet.crop(494,191,16,16);//right
            bound[14] = pacmanSpriteSheet.crop(479,208,16,16);//top
            bound[15] = pacmanSpriteSheet.crop(479,223,16,16);//bottom

            pacmanRight[0] = pacmanSpriteSheet.crop(473,1,12,13);
            pacmanRight[1] = pacmanSpriteSheet.crop(489,1,13,13);

            pacmanLeft[0] = pacmanSpriteSheet.crop(474,17,12,13);
            pacmanLeft[1] = pacmanSpriteSheet.crop(489,1,13,13);

            pacmanUp[0] = pacmanSpriteSheet.crop(473,34,13,12);
            pacmanUp[1] = pacmanSpriteSheet.crop(489,1,13,13);

            pacmanDown[0] = pacmanSpriteSheet.crop(473,48,13,12);
            pacmanDown[1] = pacmanSpriteSheet.crop(489,1,13,13);
            
            blinkyRight[0] = pacmanSpriteSheet.crop(456, 64, 16, 16);
            blinkyRight[1] = pacmanSpriteSheet.crop(472, 64, 16, 16);
            pinkyRight[0] = pacmanSpriteSheet.crop(456, 80, 16, 16);
            pinkyRight[1] = pacmanSpriteSheet.crop(472, 80, 16, 16);
            inkyRight[0] = pacmanSpriteSheet.crop(456, 96, 16, 16);
            inkyRight[1] = pacmanSpriteSheet.crop(472, 96, 16, 16);
            clydeRight[0] = pacmanSpriteSheet.crop(456, 112, 16, 16);
            clydeRight[1] = pacmanSpriteSheet.crop(472, 112, 16, 16);
            
            blinkyLeft[0] = pacmanSpriteSheet.crop(488, 64, 16, 16);
            blinkyLeft[1] = pacmanSpriteSheet.crop(504, 64, 16, 16);
            pinkyLeft[0] = pacmanSpriteSheet.crop(488, 80, 16, 16);
            pinkyLeft[1] = pacmanSpriteSheet.crop(504, 80, 16, 16);
            inkyLeft[0] = pacmanSpriteSheet.crop(488, 96, 16, 16);
            inkyLeft[1] = pacmanSpriteSheet.crop(504, 96, 16, 16);
            clydeLeft[0] = pacmanSpriteSheet.crop(488, 112, 16, 16);
            clydeLeft[1] = pacmanSpriteSheet.crop(504, 112, 16, 16);
            
            blinkyUp[0] = pacmanSpriteSheet.crop(520, 64, 16, 16);
            blinkyUp[1] = pacmanSpriteSheet.crop(536, 64, 16, 16);
            pinkyUp[0] = pacmanSpriteSheet.crop(520, 80, 16, 16);
            pinkyUp[1] = pacmanSpriteSheet.crop(536, 80, 16, 16);
            inkyUp[0] = pacmanSpriteSheet.crop(520, 96, 16, 16);
            inkyUp[1] = pacmanSpriteSheet.crop(536, 96, 16, 16);
            clydeUp[0] = pacmanSpriteSheet.crop(520, 112, 16, 16);
            clydeUp[1] = pacmanSpriteSheet.crop(536, 112, 16, 16);
            
            blinkyDown[0] = pacmanSpriteSheet.crop(552, 64, 16, 16);
            blinkyDown[1] = pacmanSpriteSheet.crop(568, 64, 16, 16);
            pinkyDown[0] = pacmanSpriteSheet.crop(552, 80, 16, 16);
            pinkyDown[1] = pacmanSpriteSheet.crop(568, 80, 16, 16);
            inkyDown[0] = pacmanSpriteSheet.crop(552, 96, 16, 16);
            inkyDown[1] = pacmanSpriteSheet.crop(568, 96, 16, 16);
            clydeDown[0] = pacmanSpriteSheet.crop(552, 112, 16, 16);
            clydeDown[1] = pacmanSpriteSheet.crop(568, 112, 16, 16);

            intro = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/PacMan/intro.png"));
            start = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/startScreen.png"));



        }catch (IOException e) {
        e.printStackTrace();
    }


    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Images.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
