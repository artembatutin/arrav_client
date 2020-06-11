package net.arrav.util.tool.itemdef;

public class ItemDefinition {

    public ItemDefinition() {

    }

    public int id;
    public String name;
    public String description;
    public boolean stackable;
    public int value;
    public boolean noted;
    public boolean isTwoHanded;
    public boolean weapon;
    public int[] requirement;
    public EquipmentType equipmentType;
    public double attackStab = 0.0;
    public double attackSlash = 0.0;
    public double attackCrush = 0.0;
    public double attackMagic = 0.0;
    public double attackRanged = 0.0;
    public double defenceStab = 0.0;
    public double defenceSlash = 0.0;
    public double defenceCrush = 0.0;
    public double defenceMagic = 0.0;
    public double defenceRanged = 0.0;
    public double defenceSummoning = 0.0;
    public double absorbMelee = 0.0;
    public double absorbMagic = 0.0;
    public double absorbRanged = 0.0;
    public double bonusStrength = 0.0;
    public double bonusRanged = 0.0;
    public double bonusPrayer = 0.0;
    public double bonusMagic = 0.0;



    public enum EquipmentType {
        HAT(),
        CAPE(),
        SHIELD(),
        GLOVES(),
        BOOTS(),
        AMULET(),
        RING(),
        ARROWS(),
        FULL_MASK(),
        FULL_HELMET(),
        BODY(),
        PLATEBODY(),
        LEGS(),
        WEAPON();

        private EquipmentType() {
        }
    }

}
