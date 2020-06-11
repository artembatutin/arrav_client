package net.arrav.util.tool.itemdef;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.arrav.cache.unit.ObjectType;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.graphic.Viewport;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.net.SignLink;
import net.arrav.util.DataToolkit;
import net.arrav.util.io.Buffer;
import net.arrav.util.string.JsonSaver;
import net.arrav.world.model.Model;
import net.arrav.world.model.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class ItemDefController implements Initializable {


    public TextField itemName, modelID, maleEquip, femaleEquip;
    public Slider modelZoom, rotationX, rotationY, translationX, translationY, opcaity;
    public TextField itemSearch;
    public ListView<ObjectType> definitionsList;
    public ImageView sprite;
    public Button reset, save;
    public CheckBox wearable;

    private ObjectType selectedDefinition;

    private ObjectType selectedClone;

    private Image loading = new Image(ClassLoader.getSystemClassLoader().getResource("dev/loading.gif").toString());



    public void initDefs() {
        ArrayList<ObjectType> defs = new ArrayList<>();
        for(int i = 0; i < ObjectType.length; i++) {
            ObjectType def = ObjectType.get(i);
            defs.add(def.clone());
        }
        cache = defs;
    }

    public static ArrayList<ObjectType> cache = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sprite.setImage(loading);
        initDefs();
        cache.stream().filter(Objects::nonNull).forEach(definitionsList.getItems()::add);
        FilteredList<ObjectType> filteredData = new FilteredList<>(definitionsList.getItems(), s -> true);

        definitionsList.setItems(filteredData);

        itemSearch.textProperty().addListener(obs->{
            String filter = itemSearch.getText().toLowerCase();
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            } else {
                filteredData.setPredicate(s ->  {

                    boolean numSearch = filter.matches("-?\\d+(\\.\\d+)?");
                    if(numSearch)
                        return String.valueOf(s.id).startsWith(filter);
                    return String.valueOf(s.name).toLowerCase().contains(filter);
                });
            }
        });




        definitionsList.setCellFactory(d -> new ListCell<ObjectType>() {
            @Override
            public void updateItem(ObjectType definition, boolean empty) {
                super.updateItem(definition, empty);
                if (empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    setText(definition.id+" "+definition.name);
                }
            }
        });


        this.modelZoom.valueProperty().addListener((observable, oldValue, newValue) ->  {
            this.selectedDefinition.iconZoom = newValue.intValue();
            updateSprite();
        });
        this.rotationY.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedDefinition.iconYaw = newValue.intValue();
            updateSprite();
        });
        this.rotationX.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedDefinition.iconRoll = newValue.intValue();
            updateSprite();
        });
        //this.translationZ.valueProperty().addListener((observable, oldValue, newValue) -> {
        //    this.selectedDefinition.modelOffset1 = newValue.intValue();
        //    updateSprite();
        //});
        this.translationY.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedDefinition.iconVerticalOffset = newValue.intValue();
            updateSprite();
        });
        this.translationX.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedDefinition.iconHorizontalOffset = newValue.intValue();
            updateSprite();
        });
        this.wearable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.selectedDefinition.actions = new String[]{null, "Wear", null, null, "Drop"};
                this.selectedDefinition.groundActions = new String[]{null, null, "Take", null, null};
            } else {
                this.selectedDefinition.actions = selectedClone.actions;
                this.selectedDefinition.groundActions = selectedClone.groundActions;
            }

        });
        this.opcaity.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedDefinition.diffusion = 400 - newValue.intValue();
            updateSprite();
        });

        this.itemName.textProperty().addListener((observable, oldValue, newValue) -> this.selectedDefinition.name = newValue);
        this.maleEquip.textProperty().addListener((observable, oldValue, newValue) -> this.selectedDefinition.maleEquip = Integer.parseInt(newValue));
        this.femaleEquip.textProperty().addListener((observable, oldValue, newValue) -> this.selectedDefinition.femaleEquip = Integer.parseInt(newValue));
        this.modelID.textProperty().addListener((observable, oldValue, newValue) -> this.selectedDefinition.modelId = Integer.parseInt(newValue));
    }


    @FXML
    private void selectDefinition() {
        if(definitionsList.getSelectionModel().getSelectedItem() == null)
            return;
        selectedDefinition = definitionsList.getSelectionModel().getSelectedItem();

        selectedClone = selectedDefinition.clone();
        this.sprite.setImage(loading);
        Player.modelcache.clear();
        ObjectType.modelcache.clear();
        ObjectType.iconcache.clear();
        ObjectType.defCache.clear();

        updateSprite();
        updateDefinitionGrid();
    }

    private String[] wearActions = new String[]{null, "Wear", null, null, "Drop"};
    private String[] groundActions = new String[]{null, null, "Take", null, null};

    private void updateDefinitionGrid() {
        ObjectType def = selectedDefinition;
        modelID.setText(def.modelId+"");
        maleEquip.setText(def.maleEquip+"");
        femaleEquip.setText(def.femaleEquip+"");
        itemName.setText(def.name);
        rotationX.setValue(def.iconRoll);
        rotationY.setValue(def.iconYaw);
        translationX.setValue(def.iconHorizontalOffset);
        translationY.setValue(def.iconVerticalOffset);
        opcaity.setValue(def.diffusion);
        modelZoom.setValue(def.iconZoom);

        if(def.actions != null && def.groundActions != null && Arrays.equals(def.actions, wearActions) && Arrays.equals(def.groundActions, groundActions))
            wearable.selectedProperty().setValue(true);
        else
            wearable.selectedProperty().setValue(false);

    }

    private void updateSprite() {
        if(selectedDefinition.modelId != 0) {
            Task task = new Task() {
                @Override
                protected Object call() {
                    long start = System.currentTimeMillis();
                    while (Model.fetchModel(selectedDefinition.modelId) == null) {
                        if (System.currentTimeMillis() - start > 5_000)
                            return null;
                    }
                    Model.fetchModel(selectedDefinition.modelId, selectedDefinition.dataType);
                    BitmapImage image = getIcon(selectedDefinition, 1, 5);
                    BufferedImage bi = new BufferedImage(image.imageWidth, image.imageHeight, BufferedImage.TYPE_INT_RGB);
                    bi.setRGB(0, 0, image.imageWidth, image.imageHeight, image.imageRaster, 0, image.imageWidth);
                    java.awt.Image img = makeColorTransparent(bi, new Color(0, 0, 0));
                    BufferedImage trans = imageToBufferedImage(img);
                    Image fxImage = SwingFXUtils.toFXImage(trans, null);
                    sprite.setImage(fxImage);
                    return null;
                }
            };
            createTask(task);
        }
    }


    private void createTask(Task task) {
        Platform.runLater(task);
    }


    /**
     * Turns an Image into a BufferedImage.
     * @param image
     * @return
     */
    private static BufferedImage imageToBufferedImage(java.awt.Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * Makes the specified color transparent in a buffered image.
     * @param im
     * @param color
     * @return
     */
    public static java.awt.Image makeColorTransparent(BufferedImage im, final Color color) {
        RGBImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }


    public void reset() {
        selectedDefinition = selectedClone.clone();
        updateDefinitionGrid();
        updateSprite();
    }

    public void save() {
        try {
            packValues("custom_obj");
            Buffer buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/item/custom_obj.idx"));
            System.out.println(buffer.getUShort()+" Items written!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void packValues(String name) throws IOException {
        DataOutputStream dat = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/item/"+name+".dat"));
        DataOutputStream idx = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/item/"+name+".idx"));
        idx.writeShort(cache.size());
        dat.writeShort(cache.size());

        JsonSaver saver = new JsonSaver();
        Gson gson = new Gson();

        for(int i = 0; i < cache.size(); i++) {
            ObjectType obj;
            try {
                int start = dat.size();
                obj = cache.get(i);
                obj.encode(dat);
                int end = dat.size();
                idx.writeShort(end - start);
            } catch(Exception e) {
                e.printStackTrace();
                break;
            }
            saver.current().add(i+"", gson.toJsonTree(obj));
            saver.split();
            double progress = ((double) (i + 1) / cache.size()) * 100;
            System.out.println(String.format("%.2f%s", progress, "%"));
        }
        saver.publish("./tacking/item_definitions.json");
        dat.close();
        idx.close();
    }


    public static BitmapImage getIcon(ObjectType obj, int itemAmount, int border) {
        double oldType = Rasterizer3D.renderType;
        Rasterizer3D.renderType = 10000;
        if(obj == null) {
            Rasterizer3D.renderType = oldType;
            return null;
        }
        if(obj.stackableIds == null) {
            itemAmount = -1;
        }
        if(itemAmount > 1) {
            int stackedId = -1;
            for(int j1 = 0; j1 < 10; j1++) {
                if(itemAmount >= obj.stackAmounts[j1] && obj.stackAmounts[j1] != 0) {
                    stackedId = obj.stackableIds[j1];
                }
            }
            if(stackedId != -1) {
                obj = cache.get(stackedId);
            }
        }
        final Model model = getAmountModel(obj, 1);
        if(model == null) {
            Rasterizer3D.renderType = oldType;
            return null;
        }
        BitmapImage sprite = null;
        if(obj.noteTemplateId != -1) {
            sprite = getIcon(cache.get(obj.noteId), 10, -1);
            if(sprite == null) {
                Rasterizer3D.renderType = oldType;
                return null;
            }
        }
        if(obj.lentItemID != -1) {
            try {
                sprite = getIcon(cache.get(obj.lendID), 50, 0);
            } catch(Exception ignored) {
            }
            if(sprite == null) {
                Rasterizer3D.renderType = oldType;
                return null;
            }
        }
        final BitmapImage sprite2 = new BitmapImage(32, 32);
        Viewport viewport = Rasterizer3D.viewport;
        final int[] pixels = Rasterizer2D.canvasRaster;
        final int width = Rasterizer2D.canvasWidth;
        final int height = Rasterizer2D.canvasHeight;
        final int startX = Rasterizer2D.clipStartX;
        final int endX = Rasterizer2D.clipEndX;
        final int startY = Rasterizer2D.clipStartY;
        final int endY = Rasterizer2D.clipEndY;
        Rasterizer2D.setCanvas(sprite2.imageRaster, 32, 32);
        Rasterizer2D.fillRectangle(0, 0, 32, 32, 0);
        Rasterizer3D.viewport = new Viewport(0, 0, 32, 32, 32);
        int zoom = obj.iconZoom;
        if(border == -1) {
            zoom = (int) (zoom * 1.5D);
        }
        if(border > 0) {
            zoom = (int) (zoom * 1.04D);
        }
        final int l3 = Rasterizer3D.angleSine[obj.iconYaw] * zoom >> 16;
        final int i4 = Rasterizer3D.angleCosine[obj.iconYaw] * zoom >> 16;
        model.drawModel(obj.iconRoll, obj.spriteCameraYaw, obj.iconYaw, obj.iconHorizontalOffset, l3 + model.maxVerticalDistUp / 2 + obj.iconVerticalOffset, i4 + obj.iconVerticalOffset);
        for(int _x = 31; _x >= 0; _x--) {
            for(int _y = 31; _y >= 0; _y--) {
                if(sprite2.imageRaster[_x + _y * 32] == 0) {
                    if(_x > 0 && sprite2.imageRaster[_x - 1 + _y * 32] > 1) {
                        sprite2.imageRaster[_x + _y * 32] = 1;
                    } else if(_y > 0 && sprite2.imageRaster[_x + (_y - 1) * 32] > 1) {
                        sprite2.imageRaster[_x + _y * 32] = 1;
                    } else if(_x < 31 && sprite2.imageRaster[_x + 1 + _y * 32] > 1) {
                        sprite2.imageRaster[_x + _y * 32] = 1;
                    } else if(_y < 31 && sprite2.imageRaster[_x + (_y + 1) * 32] > 1) {
                        sprite2.imageRaster[_x + _y * 32] = 1;
                    }
                }
            }
        }
        if(border > 0) {
            for(int _x = 31; _x >= 0; _x--) {
                for(int _y = 31; _y >= 0; _y--) {
                    if(sprite2.imageRaster[_x + _y * 32] == 0) {
                        if(_x > 0 && sprite2.imageRaster[_x - 1 + _y * 32] == 1) {
                            sprite2.imageRaster[_x + _y * 32] = border;
                        } else if(_y > 0 && sprite2.imageRaster[_x + (_y - 1) * 32] == 1) {
                            sprite2.imageRaster[_x + _y * 32] = border;
                        } else if(_x < 31 && sprite2.imageRaster[_x + 1 + _y * 32] == 1) {
                            sprite2.imageRaster[_x + _y * 32] = border;
                        } else if(_y < 31 && sprite2.imageRaster[_x + (_y + 1) * 32] == 1) {
                            sprite2.imageRaster[_x + _y * 32] = border;
                        }
                    }
                }
            }
        } else if(border == 0) {
            for(int _x = 31; _x >= 0; _x--) {
                for(int _y = 31; _y >= 0; _y--) {
                    if(sprite2.imageRaster[_x + _y * 32] == 0 && _x > 0 && _y > 0 && sprite2.imageRaster[_x - 1 + (_y - 1) * 32] > 0) {
                        sprite2.imageRaster[_x + _y * 32] = 0x302020;
                    }
                }
            }
        }
        if(obj.noteTemplateId != -1 && sprite != null) {
            final int maxWidth = sprite.imageOriginalWidth;
            final int maxHeight = sprite.imageOriginalHeight;
            sprite.imageOriginalWidth = 32;
            sprite.imageOriginalHeight = 32;
            sprite.drawImage(0, 0);
            sprite.imageOriginalWidth = maxWidth;
            sprite.imageOriginalHeight = maxHeight;
        }
        if(obj.lendID != -1 && sprite != null) {
            final int l5 = sprite.imageOriginalWidth;
            final int j6 = sprite.imageOriginalHeight;
            sprite.imageOriginalWidth = 32;
            sprite.imageOriginalHeight = 32;
            sprite.drawImage(0, 0);
            sprite.imageOriginalWidth = l5;
            sprite.imageOriginalHeight = j6;
        }
        Rasterizer2D.setCanvas(pixels, height, width);
        Rasterizer2D.setClip(startX, startY, endX, endY);
        Rasterizer3D.viewport = viewport;
        if(obj.stackable) {
            sprite2.imageOriginalWidth = 33;
        } else {
            sprite2.imageOriginalWidth = 32;
        }
        sprite2.imageOriginalHeight = itemAmount;
        Rasterizer3D.renderType = oldType;
        return sprite2;
    }

    public static Model getAmountModel(ObjectType obj, int amt) {
        if(obj.stackableIds != null && amt > 1) {
            int j = -1;
            for(int k = 0; k < 10; k++) {
                if(amt >= obj.stackAmounts[k] && obj.stackAmounts[k] != 0) {
                    j = obj.stackableIds[k];
                }
            }
            if(j != -1) {
                return getAmountModel(cache.get(j), 1);
            }
        }
        Model model = Model.fetchModel(obj.modelId, obj.dataType);
        if(model == null) {
            return null;
        }
        if(obj.groundScaleX != 128 || obj.groundScaleY != 128 || obj.groundScaleZ != 128) {
            model.scale(obj.groundScaleX, obj.groundScaleY, obj.groundScaleZ);
        }
        if(obj.originalModelColors != null) {
            for(int l = 0; l < obj.originalModelColors.length; l++) {
                model.replaceHsl(obj.originalModelColors[l], obj.modifiedModelColors[l]);
            }
        }
        if(obj.retextureSrc != null) {
            for(int i1 = 0; i1 < obj.retextureSrc.length; i1++) {
                model.setTexture(obj.retextureSrc[i1], obj.retextureDst[i1]);
            }
        }
        model.calculateLighting(64 + obj.ambience, 768 + obj.diffusion, -50, -10, -50, true);
        model.hoverable = true;
        return model;
    }



}
