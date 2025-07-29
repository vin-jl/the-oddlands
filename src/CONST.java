public final class CONST {
    public static final int TILE_SIZE = 32;
    public static final int FRAME_WIDTH = TILE_SIZE * 25; //800
    public static final int FRAME_HEIGHT = TILE_SIZE * 20; //640
    public static final int FRAME_TIME = 10;

    public static final int PLAYER_INTERACT_WINDOW = 10;
    public static final int PLAYER_SPEED = 2;
    public static final int PLAYER_SCREEN_X = CONST.FRAME_WIDTH/2 - CONST.TILE_SIZE/2;
    public static final int PLAYER_SCREEN_Y = CONST.FRAME_HEIGHT/2 - CONST.TILE_SIZE/2;
    public static final String[] PLAYER_IMAGE_DIRECTORIES = new String[]{"resources/basil_up_0.png", "resources/basil_up_1.png", "resources/basil_up_2.png", "resources/basil_left_0.png", "resources/basil_left_1.png", "resources/basil_left_2.png", "resources/basil_down_0.png", "resources/basil_down_1.png", "resources/basil_down_2.png", "resources/basil_right_0.png", "resources/basil_right_1.png", "resources/basil_right_2.png"};

    public static final int WORLD_MAX_ROWS = 100;
    public static final int WORLD_MAX_COLS = 100;
    public static final int WORLD_WIDTH = WORLD_MAX_COLS * TILE_SIZE;
    public static final int WORLD_HEIGHT = WORLD_MAX_ROWS * TILE_SIZE;

    public static final int DIALOGUE_BOX_X = 40;
    public static final int DIALOGUE_BOX_Y = 380;
    public static final int DIALOGUE_BOX_HEIGHT = 200;
    public static final int DIALOGUE_BOX_WIDTH = 720;

    public static final Item ITEM_KEY = new Item("Key", "Unlocks doors.", "resources/key.png");
    public static final Item ITEM_PENGUIN = new Item("Penguin", "Why do you have this?", "resources/key.png");

    public static final BattleSkill BASIL_SKILL_ONE = new BattleSkill("Herbal Slam", "Deals a large amount of damage to the enemy.", BattleSkillType.ATTACK, BattleTarget.ENEMY_ONE, new String[]{}, new int[]{}, 1.6, 20);
    public static final BattleSkill BASIL_SKILL_TWO = new BattleSkill("Thorns", "Drains 50 hp to deal a huge amount of damage.", BattleSkillType.ATTACK, BattleTarget.ENEMY_AND_SELF, new String[]{"hp"}, new int[]{-50}, 2.5, 50);
    public static final BattleSkill BASIL_SKILL_THREE = new BattleSkill("Tulip", "Attack, then boost self defence by 10.", BattleSkillType.ATTACK, BattleTarget.ENEMY_AND_SELF, new String[]{"def"}, new int[]{10}, 1,30);
    public static final BattleSkill BASIL_SKILL_FOUR = new BattleSkill("Cheer", "Heals an ally for 50 hp and greatly improves their speed.", BattleSkillType.SUPPORT, BattleTarget.ALLY_ONE, new String[]{"hp", "spd"}, new int[]{50, 30}, 0,40);
    public static final BattleSkill AUBREY_SKILL_ONE = new BattleSkill("Headbutt", "Deals a huge amount of damage to the enemy and reduces user defence.", BattleSkillType.ATTACK, BattleTarget.ENEMY_AND_SELF, new String[]{"def"}, new int[]{-10}, 2.5, 40);
    public static final BattleSkill AUBREY_SKILL_TWO = new BattleSkill("Celebration", "Boosts all of the user's stats by 5.", BattleSkillType.ATTACK, BattleTarget.ENEMY_AND_SELF, new String[]{"atk", "def", "spd", "luck"}, new int[]{5, 5, 5, 5}, 1, 30);
    public static final BattleSkill AUBREY_SKILL_THREE = new BattleSkill("Birthday", "Greatly boosts the speed of all party members.", BattleSkillType.SUPPORT, BattleTarget.ALLY_ALL, new String[]{"spd"}, new int[]{15}, 0, 25);
    public static final BattleSkill AUBREY_SKILL_FOUR = new BattleSkill("Mood Wrecker", "Attacks the enemy and reduces its attack.", BattleSkillType.DEBUFF, BattleTarget.ENEMY_ONE, new String[]{"atk"}, new int[]{5}, 2, 35);
    public static final BattleSkill KEL_SKILL_ONE = new BattleSkill("Chicken Ball", "Reduces the enemy's speed and attack.", BattleSkillType.DEBUFF, BattleTarget.ENEMY_ONE, new String[]{"spd", "def"}, new int[]{5, 5}, 1.2, 20);
    public static final BattleSkill KEL_SKILL_TWO = new BattleSkill("Piercing Shot", "Greatly reduces the enemy's defence.", BattleSkillType.DEBUFF, BattleTarget.ENEMY_ONE, new String[]{"def"}, new int[]{20}, 0, 50);
    public static final BattleSkill KEL_SKILL_THREE = new BattleSkill("Unlucky Present", "Greatly reduces the enemy's luck.", BattleSkillType.DEBUFF, BattleTarget.ENEMY_ONE, new String[]{"luck"}, new int[]{10}, 1,60);
    public static final BattleSkill KEL_SKILL_FOUR = new BattleSkill("Run 'n Gun", "Attacks the enemy and increase user attack.", BattleSkillType.ATTACK, BattleTarget.ENEMY_AND_SELF, new String[]{"atk"}, new int[]{5}, 1.1,25);
    public static final BattleSkill HERO_SKILL_ONE = new BattleSkill("Heal Pulse", "Heals all party members by 80 hp.", BattleSkillType.SUPPORT, BattleTarget.ALLY_ALL, new String[]{"hp"}, new int[]{80}, 0, 40);
    public static final BattleSkill HERO_SKILL_TWO = new BattleSkill("Guardian Angel", "Heals one ally by 150 hp, and increase their def.", BattleSkillType.SUPPORT, BattleTarget.ALLY_ONE, new String[]{"hp", "def"}, new int[]{150, 10}, 0, 80);
    public static final BattleSkill HERO_SKILL_THREE = new BattleSkill("Lunar Blessing", "Restores 60 mana to all party members.", BattleSkillType.SUPPORT, BattleTarget.ALLY_ALL, new String[]{"mana"}, new int[]{60}, 0, 80);
    public static final BattleSkill HERO_SKILL_FOUR = new BattleSkill("Overabundance", "Restores 200 mana to one party member.", BattleSkillType.SUPPORT, BattleTarget.ALLY_ONE, new String[]{"mana"}, new int[]{200}, 0, 80);

    public static final BattleCharacter BASIL = new BattleCharacter("Basil", 300, 180, 50, 10, 25, 20, "resources/basil_battle.png", new BattleSkill[]{BASIL_SKILL_ONE, BASIL_SKILL_TWO, BASIL_SKILL_THREE, BASIL_SKILL_FOUR});
    public static final BattleCharacter AUBREY = new BattleCharacter("Aubrey", 280, 160, 80, 10, 30, 30, "resources/aubrey_battle.png", new BattleSkill[]{AUBREY_SKILL_ONE, AUBREY_SKILL_TWO, AUBREY_SKILL_THREE, AUBREY_SKILL_FOUR});
    public static final BattleCharacter KEL = new BattleCharacter("Kel", 320, 220, 30, 10, 80, 10, "resources/kel_battle.png", new BattleSkill[]{KEL_SKILL_ONE, KEL_SKILL_TWO, KEL_SKILL_THREE, KEL_SKILL_FOUR});
    public static final BattleCharacter HERO = new BattleCharacter("Hero", 400, 240, 10, 10, 10, 5, "resources/hero_battle.png", new BattleSkill[]{HERO_SKILL_ONE, HERO_SKILL_TWO, HERO_SKILL_THREE, HERO_SKILL_FOUR});

    public static final BattleSkill SPIDER_SKILL_ONE = new BattleSkill("Small Bite", "does like 2 damage", BattleSkillType.ATTACK, BattleTarget.ENEMY_ONE, new String[]{}, new int[]{}, 1.25, 0);
    public static final BattleSkill SPIDER_SKILL_TWO = new BattleSkill("Big Bite", "does like 3 damage", BattleSkillType.ATTACK, BattleTarget.ENEMY_ONE, new String[]{}, new int[]{}, 1.8, 0);
    public static final BattleSkill SPIDER_SKILL_THREE = new BattleSkill("Sticky Web", "slows all enemies", BattleSkillType.DEBUFF, BattleTarget.ENEMY_ALL, new String[]{"spd"}, new int[]{5}, 1.1, 0);
    public static final BattleSkill SPIDER_SKILL_FOUR = new BattleSkill("Fang", "does 2 damage to all", BattleSkillType.ATTACK, BattleTarget.ENEMY_ALL, new String[]{}, new int[]{}, 1.33, 0);

    public static final BattleEnemy ENEMY_SPIDER = new BattleEnemy("Spider", 1000, 0, 70, 5, 20, 5, "resources/spider_battle.png", new BattleSkill[]{SPIDER_SKILL_ONE, SPIDER_SKILL_TWO, SPIDER_SKILL_THREE, SPIDER_SKILL_FOUR});
    public static final String[] ENEMY_SPIDER_IMAGE_DIRECTORIES = new String[]{"resources/ow_spider_1.png", "resources/ow_spider_2.png", "resources/ow_spider_3.png", "resources/ow_spider_4.png", "resources/ow_spider_5.png", "resources/ow_spider_6.png"};

}
