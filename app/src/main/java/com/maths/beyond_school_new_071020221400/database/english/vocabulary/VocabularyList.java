package com.maths.beyond_school_new_071020221400.database.english.vocabulary;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyCategoryModel;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyModel;

import java.util.ArrayList;

@Keep
public abstract class VocabularyList {


    public abstract static class GradeOneVocabulary {
        public static VocabularyModel englishListGrade1() {

            ArrayList<VocabularyDetails> vocabularyDetailBathroom = getVocabularyDetailsBathroom();
            ArrayList<VocabularyDetails> vocabularyDetailBathroom1 = getVocabularyDetailsBathroom1();

            ArrayList<VocabularyDetails> vocabularyDetailBody = getVocabularyDetailsBodyParts();
            ArrayList<VocabularyDetails> vocabularyDetailBody1 = getVocabularyDetailsBodyParts1();

            ArrayList<VocabularyDetails> vocabularyDetailColor = getVocabularyDetailsColors();
            ArrayList<VocabularyDetails> vocabularyDetailColor1 = getVocabularyDetailsColors1();

            ArrayList<VocabularyDetails> vocabularyDetailAnimals = getVocabularyDetailsAnimals();
            ArrayList<VocabularyDetails> vocabularyDetailAnimals1 = getVocabularyDetailsAnimals1();

            ArrayList<VocabularyDetails> vocabularyDetailFruits = getVocabularyDetailsFruits();
            ArrayList<VocabularyDetails> vocabularyDetailFruits1 = getVocabularyDetailsFruits1();

            ArrayList<VocabularyDetails> vocabularyDetailVegetables = getVocabularyDetailsVegetables();
            ArrayList<VocabularyDetails> vocabularyDetailVegetables1 = getVocabularyDetailsVegetables1();

            var vocabularyDetailCloth = getVocabularyDetailsCloths();
            var vocabularyDetailCloth1 = getVocabularyDetailsCloths1();
            var vocabularyDetailFeeling = getVocabularyDetailsFeelings();
            var vocabularyDetailFeeling1 = getVocabularyDetailsFeelings1();
            var vocabularyDetailInsert = getVocabularyDetailsInsect();
            var vocabularyDetailInsert1 = getVocabularyDetailsInsect1();
            var vocabularyDetailKitchen = getVocabularyDetailsKitchen();
            var vocabularyDetailKitchen1 = getVocabularyDetailsKitchen1();
            var vocabularyDetailsLivingRoom = getVocabularyDetailsLivingRoom();


            var vocabularyDetailSchool = getVocabularyDetailsSchool();
            var vocabularyDetailSummer = getVocabularyDetailsSummer();
            var vocabularyDetailTown = getVocabularyDetailsTown();
            var vocabularyDetailTransport = getVocabularyDetailsTransport();
            var vocabularyDetailWeather = getVocabularyDetailsWeather();


            var listVocabulary = new ArrayList<VocabularyCategoryModel>();
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "bathroom_1",
                            vocabularyDetailBathroom
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "bathroom_2",
                            vocabularyDetailBathroom1
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "body_parts_1",
                            vocabularyDetailBody
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "body_parts_2",
                            vocabularyDetailBody1
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "colors_1",
                            vocabularyDetailColor
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "colors_2",
                            vocabularyDetailColor1
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "animals_1",
                            vocabularyDetailAnimals
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "animals_2",
                            vocabularyDetailAnimals1
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "fruits_1",
                            vocabularyDetailFruits
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "fruits_2",
                            vocabularyDetailFruits1
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "bathroom_1",
                            vocabularyDetailBathroom
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "bathroom_2",
                            vocabularyDetailBathroom1
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "vegetables_1",
                            vocabularyDetailVegetables
                    )
            );
            listVocabulary.add(
                    new VocabularyCategoryModel(
                            "vegetables_2",
                            vocabularyDetailVegetables1
                    )
            );

            listVocabulary.add(new VocabularyCategoryModel("cloth_1", vocabularyDetailCloth));
            listVocabulary.add(new VocabularyCategoryModel("cloth_2", vocabularyDetailCloth1));
            listVocabulary.add(new VocabularyCategoryModel("feeling_1", vocabularyDetailFeeling));
            listVocabulary.add(new VocabularyCategoryModel("feeling_2", vocabularyDetailFeeling1));
            listVocabulary.add(new VocabularyCategoryModel("insect_1", vocabularyDetailInsert));
            listVocabulary.add(new VocabularyCategoryModel("insect_2", vocabularyDetailInsert1));
            listVocabulary.add(new VocabularyCategoryModel("kitchen_1", vocabularyDetailKitchen));
            listVocabulary.add(new VocabularyCategoryModel("kitchen_2", vocabularyDetailKitchen1));
            listVocabulary.add(new VocabularyCategoryModel("living_room", vocabularyDetailsLivingRoom));

            listVocabulary.add(new VocabularyCategoryModel("school", vocabularyDetailSchool));
            listVocabulary.add(new VocabularyCategoryModel("summer", vocabularyDetailSummer));
            listVocabulary.add(new VocabularyCategoryModel("town", vocabularyDetailTown));
            listVocabulary.add(new VocabularyCategoryModel("transport", vocabularyDetailTransport));
            listVocabulary.add(new VocabularyCategoryModel("weather", vocabularyDetailWeather));

            return new VocabularyModel(
                    1,
                    listVocabulary
            );
        }

        //    @NonNull
//    private static ArrayList<VocabularyDetails> getVocabularyDetailsOpposites() {
//        var vocabularyDetail = new ArrayList<VocabularyDetails>();
//
//        return vocabularyDetail;
//    }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsCloths() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cap",
                            "A kind of soft, flat hat, typically with a peak. It is used to cover our head.",
                            "https://www.anglomaniacy.pl/img/xa-cap.png.pagespeed.ic.jfjxV2Px78.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Coat",
                            "A piece of outer clothing with long sleeves, usually worn to keep us warm.",
                            "https://www.anglomaniacy.pl/img/xv-coat.png.pagespeed.ic.0YJylO7S5s.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Dress",
                            "A one-piece garment consisting of a top and skirt that is usually worn by a woman.",
                            "https://www.anglomaniacy.pl/img/xv-dress.png.pagespeed.ic.IHd8g1j_Ap.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Jacket",
                            "A short coat used as a piece of outer clothing.",
                            "https://www.anglomaniacy.pl/img/xa-jacket.png.pagespeed.ic.mTbNEa9Hou.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Blouse",
                            "A piece of clothing worn on the upper body, usually by women and girls.",
                            "https://www.anglomaniacy.pl/img/xv-blouse.png.pagespeed.ic.PVpPh1srSL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Scarf",
                            "A long piece of cloth or knitted material worn around the neck, head, or shoulders.",
                            "https://www.anglomaniacy.pl/img/xa-scarf.png.pagespeed.ic.zG1z4xgSoL.webp"
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsCloths1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shirt",
                            "A long piece of cloth or knitted material worn around the neck, head, or shoulders.",
                            "https://www.anglomaniacy.pl/img/xv-shirt.png.pagespeed.ic.RRSKEE-VSA.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shoes",
                            "A protective covering for the human foot, often made of leather or canvas.",
                            "https://www.anglomaniacy.pl/img/a-shoes.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Skirt",
                            "A piece of clothing that hangs from the waist and is worn by a woman or girl.",
                            "https://www.anglomaniacy.pl/img/xv-skirt.png.pagespeed.ic.E12pEkg92N.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Socks",
                            "A covering for the foot made of a woven or knitted material. ",
                            "https://www.anglomaniacy.pl/img/a-socks.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sweater",
                            "A knitted shirt, pullover, or jacket. It keeps us warm on cold days.",
                            "https://www.anglomaniacy.pl/img/xv-sweater.png.pagespeed.ic.lnjVXMhJeN.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Trouser",
                            "A cloth that cover the lower part of the body from the waist to the foot and include separate sections for each leg.",
                            "https://www.anglomaniacy.pl/img/xa-trousers.png.pagespeed.ic.Q2gdJF-qYa.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsFeelings() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Angry",
                            "A strong feeling of displeasure.",
                            "https://www.anglomaniacy.pl/img/xv-angry.png.pagespeed.ic.IoefhbyxXY.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bored",
                            "Dull, not interesting or exciting.",
                            "https://www.anglomaniacy.pl/img/xv-bored.png.pagespeed.ic.TB0CGcKCu8.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cold",
                            "Suffering from lack of warmth.",
                            "https://www.anglomaniacy.pl/img/xv-cold-f.png.pagespeed.ic.UWKs1Dui8U.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Happy",
                            "Feeling of joy or pleasure; being glad.",
                            "https://www.anglomaniacy.pl/img/xv-happy.png.pagespeed.ic.crNMKF58iO.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hot",
                            "Holding or giving off great heat.",
                            "https://www.anglomaniacy.pl/img/xv-hot-f.png.pagespeed.ic.zXEDzj-3Wy.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sad",
                            "Unhappy or without joy.",
                            "https://www.anglomaniacy.pl/img/xv-sad.png.pagespeed.ic.BgsB5n1Pv5.webp"
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsFeelings1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Scared",
                            "Feeling fear; afraid. ",
                            "https://www.anglomaniacy.pl/img/xv-scared.png.pagespeed.ic.guK4o67McZ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sky",
                            "Not comfortable with other people.",
                            "https://www.anglomaniacy.pl/img/xv-shy.png.pagespeed.ic.x-BOAnUgjd.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sleepy",
                            "In need of or starting to sleep.",
                            "https://www.anglomaniacy.pl/img/xv-sleepy.png.pagespeed.ic.Jx1DizzNpi.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Surprised",
                            "Shocked or amazed by something which is not expected.",
                            "https://www.anglomaniacy.pl/img/xv-surprised.png.pagespeed.ic.Ocjqg4p3Jb.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tried",
                            "Without strength or energy.",
                            "https://www.anglomaniacy.pl/img/xv-tired.png.pagespeed.ic.tVdQkncNM0.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Worried",
                            "Feeling trouble about something that might happen.",
                            "https://www.anglomaniacy.pl/img/xv-worried.png.pagespeed.ic.zAR1AFzT45.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsInsect() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Ant",
                            "An insect that lives in large, organized groups called colonies.",
                            "https://www.anglomaniacy.pl/img/a-ant.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bee",
                            "A bee is an insect with a yellow-and-black striped body that makes a buzzing noise as it flies. Bees make honey, and can sting.",
                            "https://www.anglomaniacy.pl/img/xv-bee.png.pagespeed.ic.YounKdLadq.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Beetle",
                            "A type of insect with wings that form a hard cover on its back when it is not flying. It has biting mouthpart.",
                            "https://www.anglomaniacy.pl/img/xv-beetle.png.pagespeed.ic.CtwoLowprK.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Butterfly",
                            "Butterflies are closely related to moths but have thinner bodies and are usually more brightly colored.",
                            "https://www.anglomaniacy.pl/img/a-butterfly.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Centipede",
                            "A small animal with a narrow body like a worm. It has many legs.",
                            "https://www.anglomaniacy.pl/img/xv-centipede.png.pagespeed.ic.UOrxyUSYZr.webp"
                    )
            );
            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsInsect1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Ant",
                            "An insect that lives in large, organized groups called colonies.",
                            "https://www.anglomaniacy.pl/img/a-ant.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bee",
                            "A bee is an insect with a yellow-and-black striped body that makes a buzzing noise as it flies. Bees make honey, and can sting.",
                            "https://www.anglomaniacy.pl/img/xv-bee.png.pagespeed.ic.YounKdLadq.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Beetle",
                            "A type of insect with wings that form a hard cover on its back when it is not flying. It has biting mouthpart.",
                            "https://www.anglomaniacy.pl/img/xv-beetle.png.pagespeed.ic.CtwoLowprK.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Butterfly",
                            "Butterflies are closely related to moths but have thinner bodies and are usually more brightly colored.",
                            "https://www.anglomaniacy.pl/img/a-butterfly.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Centipede",
                            "A small animal with a narrow body like a worm. It has many legs.",
                            "https://www.anglomaniacy.pl/img/xv-centipede.png.pagespeed.ic.UOrxyUSYZr.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsKitchen() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cooker",
                            "Small utensil, or device used for cooking.",
                            "https://www.anglomaniacy.pl/img/xv-cooker.png.pagespeed.ic.WxEVYqLbIR.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cup",
                            "A small container used for drinking.",
                            "https://www.anglomaniacy.pl/img/xv-cup.png.pagespeed.ic.t0IZcBQbV6.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Fork",
                            "A tool with a handle and two or more points. They are used for eating solid food.",
                            "https://www.anglomaniacy.pl/img/v-fork.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Fridge",
                            "A room, box, or object where food is kept cold to prevent spoiling.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Kettle",
                            "Object used to make and then pour tea or other hot drinks.",
                            "https://www.anglomaniacy.pl/img/xa-kettle.png.pagespeed.ic.5paETIDc5e.webp"
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsKitchen1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Knife",
                            "A tool with a handle and a thin, sharp blade, used for cutting.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Mug",
                            "A large and deep cup with a handle.",
                            "https://www.anglomaniacy.pl/img/xa-mug.png.pagespeed.ic.4m5OUwIshd.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pan",
                            "An open, shallow metal container for cooking or baking.",
                            "https://www.anglomaniacy.pl/img/xv-pan.png.pagespeed.ic.5ZMTqjug8s.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Plate",
                            "A flat dish on which we serve food.",
                            "https://www.anglomaniacy.pl/img/xv-plate.png.pagespeed.ic.fqYfcQdsnF.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pot",
                            "Deep and round, used for cooking soups and other food.",
                            "https://www.anglomaniacy.pl/img/xv-pot.png.pagespeed.ic.B_nyrwt3Iv.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Spoon",
                            "A tool with a small, shallow bowl at the end of a handle, used for eating, stirring, serving, or measuring.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Teapot",
                            "Used for brewing tea.",
                            "https://www.anglomaniacy.pl/img/xv-teapot.png.pagespeed.ic.4UJA7nNYNE.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsLivingRoom() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Armchair",
                            "A chair with a back and arms.",
                            "https://www.anglomaniacy.pl/img/xv-armchair.png.pagespeed.ic.3gDh8VV2vy.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Carpet",
                            "A floor covering.",
                            "https://www.anglomaniacy.pl/img/xv-carpet.png.pagespeed.ic.GEpaHiC3yn.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Clock",
                            "Shows current time.",
                            "https://www.anglomaniacy.pl/img/xv-clock.png.pagespeed.ic.swU-Qq1ToJ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Curtains",
                            "Hang in a window to decorate a room.",
                            "https://www.anglomaniacy.pl/img/xa-window.png.pagespeed.ic.0VSdNMRLv0.webp"
                    )
            );
            return vocabularyDetail;
        }

        //
        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsVegetables() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cabbage",
                            "A vegetable with large green or purple leaves that form a round head.",
                            "https://www.anglomaniacy.pl/img/xv-cabbage.png.pagespeed.ic.UZO89Qu5NY.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Carrot",
                            "A plant that produces an orange root that is eaten as a vegetable.",
                            "https://www.anglomaniacy.pl/img/xa-carrot.png.pagespeed.ic.UlnbqiVWK3.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cauliflower",
                            "A large, round, white vegetable surrounded by green leaves.",
                            "https://www.anglomaniacy.pl/img/xv-cauliflower.png.pagespeed.ic.aalIHOEd49.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Celery",
                            "A plant with crisp, pale green stalks that are eaten as a vegetable. Celery leaves and seeds are used as seasoning.",
                            "https://www.anglomaniacy.pl/img/xv-celery.png.pagespeed.ic.3nJ9FnXlQ-.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cucumber",
                            "A long vegetable shaped like a cylinder with hard green skin and white flesh. Cucumbers are eaten raw.",
                            "https://www.anglomaniacy.pl/img/xv-cucumber.png.pagespeed.ic.50uPDxIdE9.webp"
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsVegetables1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Leek",
                            "A plant related to the onion. It has wide, green leaves and a long white bulb.",
                            "https://www.anglomaniacy.pl/img/xv-leek.png.pagespeed.ic.vZ3RVknXS2.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Lettuce",
                            "Leaves of this plant used as food in salad or sandwiches.",
                            "https://www.anglomaniacy.pl/img/v-lettuce.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Onion",
                            "A round bulb with a sharp taste and smell. Onions are used in cooking and as a flavoring.",
                            "https://www.anglomaniacy.pl/img/xa-onion.png.pagespeed.ic.hMWFk2iiJ-.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Parsley",
                            "Its leaves are used to decorate foods.",
                            "https://www.anglomaniacy.pl/img/xv-parsley.png.pagespeed.ic.PWa8dpDZ2t.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Peas",
                            "A small, round, green seed grown in a pod on a climbing plant.",
                            "https://www.anglomaniacy.pl/img/xv-peas.png.pagespeed.ic.zqr6iziFXs.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Potato",
                            "A kind of thick, underground plant stem that is eaten as a vegetable.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tomato",
                            "A red or yellow fruit with a juicy pulp. A tomato is eaten either raw or cooked as a vegetable.",
                            "https://www.anglomaniacy.pl/img/xa-tomato.png.pagespeed.ic.ZMlSNW2BBw.webp"
                    )
            );

            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsFruits() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Apple",
                            "A round, firm, fleshy, edible fruit with a green, yellow, or red skin and small seeds.",
                            "https://www.anglomaniacy.pl/img/xa-apple.png.pagespeed.ic.0RezGjCEt9.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Banana",
                            "A long, curved fruit that has a thick yellow skin.",
                            "https://www.anglomaniacy.pl/img/xv-banana.png.pagespeed.ic.Y3sEGwwf2J.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cherry",
                            "A small, round fruit that grows on a tree. It is red, yellow, or purple in color and has a hard pit in the center.",
                            "https://www.anglomaniacy.pl/img/a-cherry.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Grapes",
                            "A small, juicy fruit, with a smooth skin that is either green, red, or purple. Grapes grow in bunches on woody vines.",
                            "https://www.anglomaniacy.pl/img/xa-grapes.png.pagespeed.ic.tvj0f-cSPl.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Lemon",
                            "A small citrus fruit with yellow skin and sour juice.",
                            "https://www.anglomaniacy.pl/img/xv-lemon.png.pagespeed.ic.k3wkdO_efN.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Orange",
                            "A round fruit with a reddish yellow peel. It is sweet and juicy on the inside.",
                            "https://www.anglomaniacy.pl/img/xa-orange.png.pagespeed.ic.YIZoYKL7lG.webp"
                    )
            );
            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsFruits1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Peach",
                            "A round, sweet fruit that has white or yellow flesh, soft yellow or pink skin, and a large, hard seed at the center.",
                            "https://www.anglomaniacy.pl/img/xa-peach.png.pagespeed.ic.NpEgV4bTtE.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pear",
                            "A sweet, juicy fruit that is shaped like a bell.",
                            "https://www.anglomaniacy.pl/img/xa-pear.png.pagespeed.ic.3C9z6q6420.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pineapple",
                            "A large, juicy fruit, shaped like an egg, with sweet yellow flesh.",
                            "https://www.anglomaniacy.pl/img/xa-pineapple.png.pagespeed.ic.2oIW73lYRL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Plum",
                            "A type of fruit that has smooth red, purple, green, or yellow skin. It has sweet, juicy flesh and a small, smooth pit.",
                            "https://www.anglomaniacy.pl/img/xv-plum.png.pagespeed.ic.Asvtk4uq7i.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Strawberry",
                            "A red, juicy fruit with seeds on the outside.",
                            "https://www.anglomaniacy.pl/img/xv-strawberry.png.pagespeed.ic.TZbAiPq5KZ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Watermelon",
                            "A large, round fruit with a green rind and sweet pulp that is red or pink. Watermelons have many seeds.",
                            "https://www.anglomaniacy.pl/img/xv-watermelon.png.pagespeed.ic.xyai5K15R7.webp"
                    )
            );
            return vocabularyDetail;
        }


        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsAnimals() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cat",
                            "A small animal with fur, four legs, a tail, and claws, usually kept as a pet or for catching mice.",
                            "https://www.anglomaniacy.pl/img/xh-kitten.png.pagespeed.ic.R5H4s8fnem.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cow",
                            "A large female animal that is kept on farms for its milk.",
                            "https://www.anglomaniacy.pl/img/xa-cow.png.pagespeed.ic.WsYtPWR47_.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Donkey",
                            "Donkeys are smaller than horses and have longer ears. They are sturdy animals used for riding and carrying loads.",
                            "https://www.anglomaniacy.pl/img/xa-donkey.png.pagespeed.ic.LLTkbD1JrI.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Duck",
                            "A bird that lives in or near water and has webbed feet for swimming.",
                            "https://www.anglomaniacy.pl/img/xa-duck.png.pagespeed.ic.ziCtSB021B.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Goat",
                            "Mammals that are raised for their milk and meat. They are closely related to sheep.",
                            "https://www.anglomaniacy.pl/img/xa-goat.png.pagespeed.ic.YBK3Svr3YE.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Goose",
                            "A water bird that looks like a duck but is usually larger, with a longer neck and legs and a more pointed bill.",
                            "https://www.anglomaniacy.pl/img/xa-goose.png.pagespeed.ic.Owp7nto9C2.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hen",
                            "An adult female chicken. It is raised for their meat and eggs.",
                            "https://www.anglomaniacy.pl/img/xa-hen.png.pagespeed.ic.N08juYANpX.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Horse",
                            "A large hoofed animal that feeds especially on grasses and is used as a work animal and for riding.",
                            "https://www.anglomaniacy.pl/img/xa-horse.png.pagespeed.ic.LHJmdvDwVc.webp"
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsAnimals1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pig",
                            "A pink, brown, or black farm animal with short legs and a curved tail, kept for its meat.",
                            "https://www.anglomaniacy.pl/img/xa-pig.png.pagespeed.ic.zAUOKKFLR5.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Rabbit",
                            "A burrowing plant-eating mammal, with long ears, long hind legs, and a short tail.",
                            "https://www.anglomaniacy.pl/img/xa-rabbit.png.pagespeed.ic.nzJRWp5o7M.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sheep",
                            "Mammals that are raised for their wool; some people also use their milk and meat. They are closely related to goats.",
                            "https://www.anglomaniacy.pl/img/xa-sheep.png.pagespeed.ic.a-klSoAUqn.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Elephant",
                            "An elephant is an enormous, four-footed animal with big ears and a long trunk.",
                            "https://clipart.world/wp-content/uploads/2020/07/cartoon-elephant.jpg"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Camel",
                            "A large animal with long legs and a long neck. They are domesticated mammals; people use camels for riding and for carrying loads across deserts. ",
                            "https://cliparting.com/wp-content/uploads/2017/01/Cute-camel-clipart-funny-pictures.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Lion",
                            "A large wild animal of the cat family with yellowish-brown fur.",
                            "https://www.dreamstime.com/illustration/lion.html"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tiger",
                            "A large, very strong mammal with short, yellow-orange fur and black stripes.",
                            "https://clipart.world/wp-content/uploads/2020/06/realistic-tiger-walking.jpg"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsColors() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Black",
                            "This is a Black balloon.",
                            "data:image/webp;base64,UklGRgAEAABXRUJQVlA4TPMDAAAv+YA1EP+gqG0jpgx683X8kU4KCtq2YVNl0O4fBW3bsKkyaPePQCCFGXwCACAAfySJRCQiqX9ZRSSUiERIImlQsG27bVs9FIIAiHLduzX/cWZFUCTch+TXiP5PgPzv/9+P1p9rV8lsMZeO0V5y3MzSmJArfmXNwayJTQ3X2JJdDRMqrrcGsxA+dVx3T34RfMEtFr8AvuBWi1OeiR2326PR3NZw221Tmy24/WJ1Fjpm2IPCTMIsk9GWq5hndbpyHTPtTlM7ZrvrKWK+UUsZM846yphz1tCBWR/6yZh31k7EzKNuEuaeNBMw+6AXh/k7rZhKoBqlZDDMOtnBcdeIA0unD9NodKOOAzwPbQQw3XVhO5VuVXGAa9HEBra7Hkyj060aIvgeWrBg7JVQKBUdeHAOKiikmgY8WAcFFADPL4N3HBo/D+DpNPgOkoHeAeBj4PueRWPnADycBl9AM5DLAN4Gvu94FG6mA3ffAy8gaqkFAE+n87/vmCRqDcDnwBuYdkPMAbg/DT5QQSCWALwMfILrQawB+Bp4JgNLywF4OA3es9lpJQAvA59gW2hVAJ8Dr3RgSFkA96fBRz4bqR3A0wj4ZlIHgLeBD0KNVAfwPfBKCI6SA3B/GnxiFCjtAJ5GHhllShnA2wgYN0oNwNfAFyUYRgDuToMfnDwhD+Bx5I1TJLQDeBl54ZQJJQDvKqiECoDPkSdOINQAnEYfSVk+0IOnYxUR6HhFRDqbIhKdeJF7UkUFWKR0kUdSlU5RBP468zh6t1Ja/JeorIhGJyqi/CVmU0Si4xURf6OJIvzvtKIH4XuooROKaiiEghoSIa+GnZCowTOqWjCMshKqMN6VkCk5JQRK0nXgOB0q6MJ5V0Em5VQQSEnTgGWVFFCF9aaAnZZ0fpZXpleFt6e3E5PGzjCL5A5hbsl5anJQa8LdUwvkpBHrhl0gFoV+42X47bSiKLCR6kYDgVQUFTZKzejAUwqixEKoihYtIa8GiXSy6NE0Mt0oQjwZL6pMVLIosxJpRhuOiBN17jSiKPQgUUSjplFoRiXiOoHuRKkbgU3UGqYXRLF5cllUW6dWRLllYkW0a+q0ilGPmDqpYkTBpk6pGFGxKRM6RMsmTyeLotNkoqg6zKRvomxXp1GtqNuWSSQjGg8z6Jso3ZWby0b0vvWbapuo3uTb6dGI9m2+kWhkBW3uV9eTlVW0qV9VC0aWMhzX0rOTBd1y+2UtbbKsbj/qxWrarCyvDzGX0n5WSoqbl4U2Z+yy2gd+3u1ibTj3WCvbz8K2VAXnd7NQO0aPdXIY35apXqCZRUq4ZFokny+Q/SKJuKOf1Q8nS+32o/x47E7+9/9fCwEA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Blue",
                            "The color of this balloon is Blue.",
                            "data:image/webp;base64,UklGRhYEAABXRUJQVlA4TAoEAAAv+YA1EPegIJLU5sG/3A7pMaGmbSQp/FludU/lA3CSgCSx/5sDg2wjFeH96Q5hBk82QFEYHYJY/08UCglf2KWkkKhHIVGQCdJOlP7+J2Sc42wmULRtq23tHNu3t30QkgBR/v9D84xjsw/piej/BMj//v8Dslw9TTkGv6B78SHmucnBK35H9SFPSlpxl2uajrIp7la3MhEtOty5i20OWlA8oIZmvxYUD6qhGS85PLBLlsseD+6z2aLi4TXarHoM0VeDRcUgNZorYKDBVs1jqL4ZqioGq9VMRTFcLUYqGHIxUcKgk4EShp3MkzDwZJyMoWfTHBj8YZiso9NsluYwfNessoHgZpQEiskkGSSzQZpj4Zo9PGh6c0QQjcbIykSzLRZQXUwRQTYaoiobrXbwoOvNUEC4WMExckYIoBxM0JSTNgsEkA4GqKBd+e0Afr46f0js9CoAnE/Xn1mgsgsA3k+db2AZyDUFcOm4gKY2bgnA56nzgwcSNwfgpeMCoo5aBvBz6vxggsxsA/DacQbVjVhTAJeOVy7aeCUAn6fOHy5IvFYALx1PILvSagBw7nhng8YqAfg8df7QSaxWAC8dF9BdWSmAc8crHyV1APg+dX7wQeG0A3jvAeGd0wLguePCaKHUAODU+cIIjdEB4KvnjdLBKAB46/mkFBh5AM8935Q8IwVw7gFlJVQBfJ8sgMqnAPjoeSZV+AQAbzYIfDYAzz0vpDY+HsC5542U56MATjZQPrAD6GRLZDblJh+sCpvjJl+sDjbBEuGvMP4mb6zWifOUnns/ZsqK//kgWCL8FSZaIrIplih/nFVLVDZiCaHr7OD4eDt4PpsdNj7BDoFPsUPhU+1Q+YhaQYWwt4JnFKwQGB1WOBg1KzRGsthgEcqbDTZOhw0OTs0GjZN4C3ghHS0QWVULVFay8FuEduQXeVV+lZes7FYhntglZqLcVKjv3HZulVvlJhuzTchnZpmdeF5e6BdehZ94Vl4MWFgVC8jGaRMTVmWk1QYSGAUxYnN8XLOCHHwOsePKZhVDNuWizRJSuBSx5cpkFWO2hcfSrCGZRxZ7JhZJLLpx2MSmC4NFjNr8+HyzijQ3OtfErlnHplksm3Rkeohtj5EdYt00rkPsm0Z1iIXTkDSJjY8BaRErZx2NZrFzXsayVLF08yNZmxh7H0cUex86BlfE4s2PYG1i9KSP5orYvfnH2puYvvjH2aqYvyyP4YtMYXL354tMY1nvSvcqU1njci9rkgmtcfnddE1NZrUdwd9MfSgyvbWE4L3/1eJ9iKXJRMdfrTLbGVces7Vco22udly9TlVBZ5qo5nq0ztOKbj9NB24YJ6nqLbROUnJ9Lk2SiBy7XqN7kbnOMfxmzPK///+IBA=="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Brown",
                            "Rosie has a Brown balloon.",
                            "data:image/webp;base64,UklGRhQEAABXRUJQVlA4TAgEAAAv+YA1EP+gkJEkqfwtz6BP4JcwGjAJ+P+txpEUg2wj1d/yDPYAnxhkG2kIRTiE95f7KpDW/UeViioVVa6omfpXKmrsItg8ikFhCIVRCEbxDhRs227bOk+Se73oJEE+zH+e+YJi4T6kJ6L/EyD/+/8PyHD1NPlc6o7uvZbs58aXqvgdtRY/KWnDj9zSdIRV8WN1DRPh8oEffmQ3B64o7lCLs58rijvV4oyXDtzxkSznK+68erNlxd1rtlmrGGJtBsuKQWo2V8FAi61cxVCrM1RTDFabmYJiuBqMFDDkYKKEQScDJQw7mSdh4Mk4HkP3pokYfDSM19GpN4s7MPzDWWUFwdUoCRSTSTxIeoO4g8Xh7FFBs5ojg2g2hlcm6m2xg+puigyy2RBN2WizQwXdaoYAwsEKB6PDCAWUiwmcclJngQLSxQANtBu/BcD3Z+c3iYVeA4Dz6fozCzR2BcDbqfMVLAs5pwAuHRfQVMctAfg4db7zQOJ2AHjquIDoQc0D+D51vjOBZ7YCeOk4g+pKzCmAS8cLF3W8EoCPU+c3FyReG4DnjgeQ3Wg5ADh3vLGBY5UAfJw6v+kkVhuA544L6G6sFMC544WPkooAvk6d73wQOC0A3npAeOG0A3jsuDDaKTkAOHU+MYJjFAF89rxSiowKgNeeD0qFUQXw2PNFqTJSAOceUFZCDcDXyQJofAKA955HUoFPAfBqg8JnBfDY80xq5VMBnHteSVU+CuBkA+UDO4COt4RnE27yziqwiTf5ZBXZFEuUv8LUm7yy2iauUnrsfZ8pK/7ng2KJ8leYbInMJlgi/HHWLNHYiCWE7mGHg0+1Q+Wz2mHlU+xQ+AQ7BD7NDo2PqBVUCFcrVEbFCoVRtEJk5KzgGMlug10orzZYOUUbRE7OBo6TVAtUIZ0tkFk1CzRWsvPbhXbml3k1fo2XbOw2IZ7YJWai3FSoL9wWbo1b4yYrs1XIe2aenVReVegHXoGfVFZVDBhYBQvIymkVEzZlpM0GUhgVMaI7+BzOChL5RLHjxmYTQzrlos4SErgEseXGZBNjup3H7qwhnocXeyYWSSy6cljFpjuDXYzq6viqs4q4Y3SHE7t6HZt6sWzSkWkU28aRRbFuGlcU+6ZRRbFwGpImsXEckAaxstfRqBc7+30sexNLuzqSzYmxl3FksXfUMRxBLO7qCDYnRk96b0cQu7t6X4sT04d6P2sT84f9PmqQKUzHz6tBpjFsP0qXJlPZ8v5TtiQT2vL+u+mWnMyqi6XeTGsJMr0tlFJr/dVea8nByUTnX20y2x5Xxtnar1E3Vwuu3qYqoDNNlDt6tM3Thu46TRE3zJPU9BbaJikdfUeaJBGJi16jS5C59rn8Zvbyv///iAQ="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Green",
                            "This balloon is Green in color.",
                            "data:image/webp;base64,UklGRhoEAABXRUJQVlA4TA4EAAAv+YA1EP+gIJLU5sG/3A7pMWGQbaT6W77BDuATg2wj1d/yDXYAnxhkG6kI7093CDN4tWkbME7ZkkOA9f9ZqlTJUqU2f1ebKlmqyB3QJqLf/wmMOWaTQMG2rbix82xX396HkASIZv7zzDdOmftIn4j+T4D87/8/IMvV05Rj8Au6Fx9inpscvOJ3VB/ypKQVP3JN01E2xY/VrUxEiw4/3MU2By0o7lBDs18LijvV0IyXHO7YJctljzv32WxRcfcabVY9huirwaJikBrNFTDQYKvmMVTfDFUVg9VqpqIYrhYjFQy5mChh0MlACcNO5kkYeDJOxtCzaQ4M/jBM1tFpNktzGL5rVtlAcDNKAsVkkgyS2SDNsXDNHh40vTkiiEZjZGWi2RYLqC6miCAbDVGVjVY7eND1ZiggXKzgGDkjBFAOJmjKSZsFAkgHA1TQrvx2AN+fnd8kdnoVAM6n688sUNkFAG+nzlewDOSaArh0XEBTG7cE4OPU+c4DiZsD8NRxAVFHLQP4PnW+M0FmtgF46TiD6kasKYBLxwsXbbwSgI9T5zcXJF4rgOeOB5BdaTUAOHe8sUFjlQB8nDq/6SRWK4DnjgvorqwUwLnjhY+SOgB8nTrf+aBw2gG89YDwzmkB8NhxYbRQagBw6nxihMboAPDZ80rpYBQAvPZ8UAqMPIDHni9KnpECOPeAshKqAL5OFkDlUwC89zySKnwCgFcbBD4bgMeeZ1IbHw/g3PNKyvNRACcbKB/YAXSyJTKbcpN3VoXNcZNPVgebYInwVxh/k1dW68R5So+97zNlxf98ECwR/goTLRHZFEuUP86qJSobsYTQdXZwfLwdPJ/NDhufYIfAp9ih8Kl2qHxEraBC2FvBMwpWCIwOKxyMmhUaI1lssAjlzQYbp8MGB6dmg8ZJvAW8kI4WiKyqBSorWfgtQjvyi7wqv8pLVnarEE/sEjNRbirUd247t8qtcpON2SbkM7PMTjwvL/QLr8JPPCsvBiysigVk47SJCasy0moDCYyCGLE5Pq5ZQQ4+h9hxZbOKIZty0WYJKVyK2HJlsoox28JjadaQzCOLPROLJBbdOGxi04XBIkZtfny+WUWaG51rYtesY9Mslk06Mj3EtsfIDrFuGtch9k2jOsTCaUiaxMbHgLSIlbOORrPYOS9jWapYuvmRrE2MvY8jir0PHYMrYvHmR7A2MXrSe3NF7N78fe1NTF/8/WxVzF+W+/BFpjC5n+eLTGNZf5TuVaayxuWnrEkmtMbld9M1NZnVdgR/M/WhyPTWEoL3/leL9yGWJhMdf7XKbGdceczWco22udpx9TpVBZ1poprr0TpPK7r9NB24YZykqrfQOknJ9bk0SSJy7HqN7kXmOsfwmzHL//7/IxI="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Grey",
                            "The color of this balloon is Grey. ",
                            "data:image/webp;base64,UklGRjAEAABXRUJQVlA4TCQEAAAv+YA1EPegqJEkpXf9W4Xj+7EJxW3bOEr2n7Veex2suG0bR8n+s9Zrr4MRM5KkQhik838NwiCswRFM0lTbMYD1/x/3mu8Z95rvyd3KjPme8Z55r/8WAQjQLR1wwMQswIADtqUA3VK2VHWvuj9QtG2rbe0c27e3DUISRRz+/0PzjGOzD+mJ6P8EyP/+/wOyXD1NLcewoHsJMbe5aTEofkcNsU1K2nCXW5qOsiruVtcyES573LnPbg5cVDygRmc/FxUPqtEZL3k8sE+WawEPHprZsuLhNdusBgwxVINlxSA1mytioNFWLmCowRmqKgar1UxFMVwtRioYcjFRwqCTgRKGncyTMPBknIahN9McGPxhmKaj02YW5zF876yyguBqlASKySQNJJtBnGfhnT0CaAZzZBDNxmjKRJstFlBdTJFBNhuiKhutdgigG8xQQLhYwTPyRoigHE3glJM6C0SQjgaooF357QB+vjp/SOz0KgCcT9efWaCyiwDeT51vYBnJOQVw6biApjpuCcDnqfODBxI3D+Cl4wKinloD8HPq/GCCxmwF8NpxBtWVmFMAl45XLup4JQCfp84fLki8NgAvHU8gu9FyAHDueGcDxyoB+Dx1/tBJrDYALx0X0N1YKYBzxysfJXUA+D51fvBB4bQDeO8B4Z3TAuC548JooeQA4NT5wgiO0QHgq+eN0sEoAnjr+aQUGQUAzz3flAIjBXDuAWUlVAF8nyyAyqcA+Oh5JlX4RABvNoh8VgDPPS+kVj4BwLnnjVTgowBONlA+sAPoNEs0NuUmH6wKm+MmX6wONtES8a8w4SZvrLaJC5Seez9myor/+SBaIv4VJlsisymWKH+cVUtUNmIJoevt4PkEOwQ+qx1WPtEOkU+xQ+FT7VD5iFpBhXCwQmAUrRAZHVY4GDkrOEay2GARyqsNVk6HDQ5OzgaOkwQLBCGdLZBZVQtUVrLwW4R25pd5VX6Vl2zsNiGe2CVmotxUqO/cdm6VW+UmK7NVyDdmjZ0EXkHoF16FnwRWQQxYWBULyMppFRNWZaTVBhIZRTGi83y8s4IcfA6x48ZmE0M65aLOElK4FLHlxmQTY7qFx+KsIY1HE3smFkksunJYxaYLg0WM6sL4grOKOD8678SuTcemTSybdGR6iG2PkR1i3TSuQ+ybRnWIhdOQNImNjwFpESs3HY02sXNbxrJUsbQLI9mcGHsfRxZ7HzoGX8TiLoxgc2L0pI/mi9jdhcfanZi+hMdZq5i/LI8Rikxh8vcXikxj2e5K9ypTWfNyL1uSCa15+d10S05m1R0x3ExDLDK9tcQYQvjVEkLMxclE519tMtsNVx6ztVyjbq52XL1NVUFnmijne7TO04buME0HbpgnqeottE5S8n0+TZKIHLteo3uRuW45/mZu8r///4gE"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Orange",
                            "Sam has an Orange balloon.",
                            "data:image/webp;base64,UklGRhgEAABXRUJQVlA4TAsEAAAv+YA1EPegJgDaBurf6h7wBRsCksT+bw4YtZHkyPxZbvpdqANwkkG2kYowhEN4f7kPoP5fYpFYJPaRuCzHdbFILL0BRFYUahQigUKkEIgUKg4UZNuO20ZfsjN5egAIQCTIj/0vNDEcC++x547o/wTY//7/AzK9+zTFUtuO4b3VEs9NrM3xG73VeFLyhk+55dORVsen9TWdiFAOfPKjhHMQquMOvQb9QnXcqdcgXj5wx0dWLjbceYuyFcfde9GsN0yxdcGKY5Je5KqYaNUqNEy1BaG6Y7LeZUqO6XoSKWHKSaKMSWeBMqad5cmYeBYnYupRmgWTX4SJPjuPsoQD0z+CKisIrqJkUMySRJCMgoSDxRH0aKDZ5CggWsSIzsSjFjuo7lIUkC1CdGfjXYcGuk2GBMJJhYPRIUIF5SpBcE4eFKggXQXooN353QC8vQy+kbjR6wBwvbz/ygKdXQXwdBn8AZaVXHAADwMPoOmBWwbwfBl84oHM7QDwdeABRA9qEcDbZfCJCSKzFcD3gSuorsSCA3gY+M7FA68M4Pky+MYFmdcG4NvAI8hutAIAXAee2CCwygCeL4NvdDKrDcC3gQfQ3Vg5gOvAdz5OagHwehl84oPE6QbgaQSEb5x2AF8GHhjtlAIAXAa/MkJgtAB4GflBaWFUAfwYeaZUGTUAX0ZeKTVGDuA6AspOqAN4vSiAzicBeBr5QirxqQB+aFD5rAC+jHwjtfJpAK4jP0g1Pg7gooHzgQ6gE5WIbNKHPLFKbJYPeWG1sKlK1L/CtA/5wWo7cY3Sl9GnM6Xifz6oStS/whQlCpukRPrjrCvR2ZgSRvfQ4eDTdGh8Vh1WPlWHyifpkPh0HTofcxXcCDcVGqOqQmW0qLAwCioERrZrsBvlVYOV06LBwiloEDhZU6AZ6aJAYdUV6Kxs57cb7cKv8Or8Oi/b2G1GPLPLzMy5uVG/cbtx69w6N1uZrUY+MovsrPFqRj/xSvyssWomYGKVFLCV02oSdmfkXQOrjKqJGA4+R1DBFj6L6bix2UzI4Fw8KGGJSzItNyabiRl2HntQwyKPaHpmFtkUXTmspunOYDdRQ5tfC6pYOGZ3BNM1+tw8mrLZZ+aLabvMbDF187wW0zfPajGF85Q8m8bLhDyZytFn49F0jvtc9m5KhzaTLZjYt3kU03vxORzJFA9tBlsw0bPf25FM99Du6xZM+tTuZ+0mf9rvoyU7hfn4fC3ZaUzbp/Jbt1PZy/5ZtmwntJf9t/mWg53VsNT2Yd5qstPbU62ttV/trdWSgp3o8qvNznbEO5eztb/Hw7m64d3bqUoYzCcqHCPez9OG4XaaFnxgOUndP8L7ScrH2JFPkpktN3+P35Kd61jqT0u0//3/RyQA"
                    )
            );
            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsColors1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pink",
                            "This is a Pink balloon.",
                            "data:image/webp;base64,UklGRvoDAABXRUJQVlA4TO0DAAAv+YA1EO+gKJKkZv3LvEwGA7ySBQJJG9e/808gaeP6d/4FAkn+tBucAP5Bv7010UFwJKwycYCCbdttW+fdB4IkAKKouXv+E80XFAv3IT0R/Z8A+d//f0Dmu6epxOAXdC8+xDI3JXjF76g+lElJK37kmqYjb4ofq1ueiBYdfriLbQ5aUDyhhma/FhRPqqEZLzk8sUuWKx5P7ovZouLpNdqsegzRV4NFxSA1mitgoMFWzWOovhmqKgar1UxZMVzNRsoYcjZRwqCTgRKGncyTMPBknIKhF9McGPxhmKKj02KW5jB816yygeBmlASKySQFJItBmmPhmj08aHpzRBCNxijKRIstFlBdTBFBNhqiKhutdvCg682QQThbwTFyRgigHEzQlJM2CwSQDgaooF357QC+Pzu/Sez0KgCcT/efWaCyCwDeTp2vYBnINQVw6biApjZuCcDHqfOdBxI3B+DWcQFRR60A+D51vjNBYbYBeOk4g+pGrCmAS8cLF228EoCPU+c3FyReK4BbxwVkV1oNAM4db2zQWCUAH6fObzqJ1Qrg1nEB3ZWVAjh3vPBRUgeAr1PnOx9kTjuAtx4Q3jktAK4dF0YLpQYAp84bIzRGB4DPnldKB6MA4LXng1Jg5AFce74oeUYK4NwDykqoAvg6WQCVTwbw3nMllfkEAK82CHw2ANeeG6mNjwdw7nkl5fkogJMNlA/sADrFEoVNfsg7q8zmeMgnq4NNsET4K4x/yCurdeI8pWvv+0xZ8T8fBEuEv8JES0Q22RL5j7NqicpGLCF0nR0cH28Hz2ezw8Yn2CHwyXbIfKodKh9RK6gQ9lbwjIIVAqPDCgejZoXGSBYbLEJ5s8HG6bDBwanZoHESbwEvpKMFIqtqgcpKFn6L0I78Iq/Kr/KSld0qxBO7xEyUmwr1ndvOrXKr3GRjtgn5wqywE8/LC/3MK/MTz8qLATOrbAHZOG1iwqqMtNpAAqMgRmyOj2tWkIPPIXZc2axiyKZctFlCMpcstlyZrGLMtvBYmjWk8Chiz8QiiUU3DpvYdGGwiFGbH59vVpHmRuea2LXo2LSIZZOOTA+x7TGyQ6ybxnWIfdOoDrFwGpImsfExIM1i5aKj0SJ2LstYliqWbn4kaxNj7+OIYu9Dx+CyWLz5EaxNjJ702VwWuzf/XHsT02f/PFsV8+flOXyWKUzu5/ks05jXH6V7lamscfkpa5IJrXH53XRNTWa1HcE/TH3IMr01h+C9/9XifYi5yUTHX60y2wV3HrO13KNtrnbcvU5VRmeaqOZ6tM7Tim4/TQceGCep6iO0TlJyfS5Nkogcu96je5a5LjH8Zizyv///iAQA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Purple",
                            "My balloon is Purple.",
                            "data:image/webp;base64,UklGRhQEAABXRUJQVlA4TAcEAAAv+YA1EPeApI0k1b/LY3dsFDx6EQrbtm34/8PZrcK2bRv+/3B2a5BtpCEcQhHeX+7LgN7dV1Xd1V3dVdVGdBF9qe4mgvf4H2AAgGH+B4ADAEXbttvUzgFubyq2z1ax/P8fmocIaB+lJ6L/EyD/+/8PSH/1NOUa24burcWa5ybHpvgdtcU8KWHFXa5hOvyuuFvd/USkeuDOj5rmIEXFA2pM9ktR8aAak/HCgQc+guVyw4O3bLaqeHitNnMNQ2zOYFUxSK3mihhotFVqGGpLhnKKwaozk1cMV72RPIbsTRQw6GCggGEH8wQMPBgnY+jZNAWDL4bJOjrNZkkHhn8kq+wguBslgGIwSQbJbJB0sDiSPRpoNnNUEK3GyMpEsy02UN1MUUG2GsIpG3V2aKDbzOBB2FvhYHQYIYJyNEFSTposEEE6GsCBtuO3APj56vwhsdBzAHA+XX9mAccuAng/db6BZSSXFMCl4wKamrgFAJ+nzg8eCNwOAC8dFxA9qGUAP6fODybIzHYArx1nUN2JJQVw6XjloolXAPB56vzhgsBrBfDa8QSyK60EAOeOdzZIrAKAz1PnD53AagXw2nEB3ZWVAjh3vPJRUgXA96nzgw88pwXAew8IL5w2AM8dF0YbpQQAp84XRkiMCoCvnjdKhVEE8NbzSSkyagCee74pNUYK4NwDykrIAfg+WQCOjwfw0fNMyvOJAN5sEPnsAJ57XkntfBqAc88bqcZHAZxsoHxgB9DJlshs/E0+WHk25SZfrAqbaIn4V5h2kzdW68Q1Ss+9HzNlxf98EC0R/wpTLVHZeEv4P86cJRwbsYTQPexw8Gl2aHx2O+x8oh0iH28Hz8fZwfERtYIK4WaFxihaITIqViiMkhUSI9lssAnl3QY7p2KDwinZIHGSZoEmpKsFKitnAcdKNn6b0K78Ki/Hz/GSld0qxAO7wEyUmwr1hdvCzXFz3GRntgv5zCyzk8arCX3Py/OTxqqJAT0rbwHZOe1iQqeM1NlAIqMoRkwHnyNZQQqfInZc2axiyKRcNFlCPBcvtlyZrGLMtPHYkjUk88hiz8AiiEV3DrvYdGOwiVFTG19LVpF0jO5IYtesY9Mslg06Mi1i2zKyItYN4ypi3zCqIhYOQ9IgNi4DUi9WzjoazWLnvI1lc2Lp1EayJjH2Mo4q9i46hsOLxVMbwZrE6EEf7fBi99Qea0liet8eZ3difr89RvMyheG4v+ZlGv16V7o4mUpXt3tZg0yoq9vvpmtIMqupxHYzbdHL9DofY2vtV1trsfokE11/tcpsZ1xZZmu7RtNcLbh6nSqPzjBR6ehRN08ruts0FdywTpLTW6ibpHD0HWGSRKQseo0uXuY61/ibNcv//v8jEgA="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Red",
                            "This balloon is Red in color.",
                            "data:image/webp;base64,UklGRgAEAABXRUJQVlA4TPMDAAAv+YA1EPdgpm2b8ida7fMI7B0FAkkb17/zr6htG6j8ie54eQAmCQSS/Gk3OCPyHOm7fQAAVcbaAOAAACjattW2ds4+CEmAKLZvb///oXnGsdmH9ET0fwLkf///AZmvnqYSg1/QvfgQy9yU4BW/o/pQJiWtuMs1TUfeFHerW56IFh3u3MU2By0oHlBDs18LigfV0IyXHB7YJcsVjwf3xWxR8fAabVY9huirwaJikBrNFTDQYKvmMVTfDFUVg9VqpqwYrmYjZQw5myhh0MlACcNO5kkYeDJOwdCLaQ4M/jBM0dFpMUtzGL5rVtlAcDNKAsVkkgKSxSDNsXDNHh40vTkiiEZjFGWixRYLqC6miCAbDVGVjVY7eND1ZsggnK3gGDkjBFAOJmjKSZsFAkgHA1TQrvx2AD9fnT8kdnoVAM6n688sUNkFAO+nzjewDOSaArh0XEBTG7cE4PPU+cEDiZsD8NJxAVFHrQD4OXV+MEFhtgF47TiD6kasKYBLxysXbbwSgM9T5w8XJF4rgJeOJ5BdaTUAOHe8s0FjlQB8njp/6CRWK4CXjgvorqwUwLnjlY+SOgB8nzo/+CBz2gG894DwzmkB8NxxYbRQagBw6nxhhMboAPDV80bpYBQAvPV8UgqMPIDnnm9KnpECOPeAshKqAL5PFkDlkwF89DyTynwCgDcbBD4bgOeeF1IbHw/g3PNGyvNRACcbKB/YAXSKJQqbfJMPVpnNcZMvVgebYInwVxh/kzdW68R5Ss+9HzNlxf98ECwR/goTLRHZZEvkP86qJSobsYTQdXZwfLwdPJ/NDhufYIfAJ9sh86l2qHxEraBC2FvBMwpWCIwOKxyMmhUaI1lssAjlzQYbp8MGB6dmg8ZJvAW8kI4WiKyqBSorWfgtQjvyi7wqv8pLVnarEE/sEjNRbirUd247t8qtcpON2SbkC7PCTjwvL/Qzr8xPPCsvBsyssgVk47SJCasy0moDCYyCGLE5Pq5ZQQ4+h9hxZbOKIZty0WYJyVyy2HJlsoox28JjadaQwqOIPROLJBbdOGxi04XBIkZtfny+WUWaG51rYteiY9Milk06Mj3EtsfIDrFuGtch9k2jOsTCaUiaxMbHgDSLlYuORovYuSxjWapYuvmRrE2MvY8jir0PHYPLYvHmR7A2MXrSR3NZ7N78Y+1NTJ/942xVzJ+Xx/BZpjC5+/NZpjGvd6V7lamscbmXNcmE1rj8brqmJrPajuBvpj5kmd6aQ/De/2rxPsTcZKLjr1aZ7YIrj9lartE2VzuuXqcqozNNVHM9WudpRbefpgM3jJNU9RZaJym5PpcmSUSOXa/RPctclxh+Mxb53/9/RAIA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Violet",
                            "She has a Violet balloon.",
                            "data:image/webp;base64,UklGRhoEAABXRUJQVlA4TA4EAAAv+YA1EOegoI0kZe7Ov9VnRhsK27Zt1PH/t9lJKyBJ7P/mACEbSSrC+dM8yiCswQfw5t+CnjghCMnomSBkpyctaIGTgPsIxDeQLAcCA/3vPpJt+uxcs/ZA0bbtuG3zNHm+eARJzPz/L20EVcJ96NyI/k+A/O//PyDL3dNUU/ALuhcfUp2bGrzid1Qf6qTEDU+5xekoq+JpdS0T4dKBJz+SmwMXFC+owdnPBcWLanDGiwde+IiWqx4v7qvZkuLlNdmseQzRN4MlxSA1mStgoMFWzmOo3hmqKQarzUxFMVwtRioYcjFRxKCjgSKGHc0TMfBonIqhV9NkDD4bpurotJrFHRj+4ayyguBqlAiK0SQVJKtB3MHicPbwoOnNkUA0GaMqE622WEB1MUUC2WSIpmy02cGDrjdDAeFihYPRYYQAysEETjmps0AA6WCABtqN3w7g56vzh8ROrwHA+XT/mQUauwDg49T5DpaBnFMAl44LaKrjFgF8njo/eCByOwDcOi4gelCrAH5OnR9MUJmtAN46zqC6EnMK4NLxxkUdrwjg89T5wwWR1wbg1nEF2Y2WA4BzxwcbOFYRwOep84dOZLUBuHVcQHdjpQDOHW98lFQG8H3q/OCDwmkH8NHzQ2jntAC4dlxAeKHkAODUeWMExygD+Op5p5QZBQDvPZ+UAiMP4NrzTckzUgDnHlBWQg3A98kCaHwKgI+eK6nCJwB4t0HgswK49txIrXw8gHPPOynPRwGcbKB8YAfQqZaobMpDPlgVNvkhX6wym2CJ8FcY/5B3VtvEeUrX3o+ZsuJ/PgiWCH+FSZZIbIolyh9nzRKNjVhC6B52OPh4O3g+qx1WPsEOgU+xQ+HT7ND4iFpBhbC3gmcUrBAYZStkRs4KjpEsNliE8mqDlVO2QebkbOA4ibeAF9LJAolVs0BjJQu/RWgnfolX49d4ycZuE+KRXWQmyk2F+s5t59a4NW6yMluFfGVW2Ynn5YV+4VX4iWflxYCFVbGArJxWMWFTRtpsIIFRECO6g8/hrCCZTxY7bmw2MaRTLuosIYVLEVtuTDYxplt4LM4aUnlUsWdkEcWiK4dVbLowWMSozo/PO6uIO0Z3OLFr1bFpFctGHZlmsW0eWRbrxnFlsW8cVRYLxyFpFBvnAWkRK1cdjVaxc13GsjSxtPMj2ZwYex9HEntnHcNRxOLOj2BzYvSor3YUsbvzr7U7MX3xr7M2MX9ZXsMXmcJ4PJ8vMo1leyrdm0xlS8uzbFEmtKXld9MtOplVl4N/mPpQZHpbCcF7/6vF+5CKk4lOv9pktivuzLO13KNurnbcvU1VQWecKHf0aJunDd1+mjIemCap6SO0TVI8+o44SSKSd71H9yJzXVP4zVTlf///EQk="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "White",
                            "The color of his balloon is White.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Yellow",
                            "This is a Yellow balloon.",
                            "data:image/webp;base64,UklGRg4EAABXRUJQVlA4TAEEAAAv+YA1EO+AoG3b5D9/wnuz0hCQJPZ/c8CgbSRH5s/y+vefB/CSQCDZnzaDA9r/I+lp1GL1HVmKpKQvJSWlqQRAcQBAcVGA4gGABSjYtt22dZ6au+8DQRIAUeY/0XxBsXAf0hPR/wmQ//3/B2S+e5pKDH5B9+JDLHNTglf8jupDmZS04keuaTrypvixuuWJaNHhh7vY5qAFxRNqaPZrQfGkGprxksMTu2S54vHkvpgtKp5eo82qxxB9NVhUDFKjuQIGGmzVPIbqm6GqYrBazZQVw9VspIwhZxMlDDoZKGHYyTwJA0/GKRh6Mc2BwR+GKTo6LWZpDsN3zSobCG5GSaCYTFJAshikORau2cODpjdHBNFojKJMtNhiAdXFFBFkoyGqstFqBw+63gwZhLMVHCNnhADKwQRNOWmzQADpYIAK2pXfDuD7s/ObxE6vAsD5dP+ZBSq7AODt1PkKloFcUwCXjgtoauOWAHycOt95IHFzAG4dFxB11AqA71PnOxMUZhuAl44zqG7EmgK4dLxw0cYrAfg4dX5zQeK1Arh1XEB2pdUA4NzxxgaNVQLwcer8ppNYrQBuHRfQXVkpgHPHCx8ldQD4OnW+80HmtAN46wHhndMC4NpxYbRQagBw6rwxQmN0APjseaV0MAoAXns+KAVGHsC154uSZ6QAzj2grIQqgK+TBVD5ZADvPVdSmU8A8GqDwGcDcO25kdr4eADnnldSno8CONlA+cAOoFMsUdjkh7yzymyOh3yyOtgES4S/wviHvLJaJ85Tuva+z5QV//NBsET4K0y0RGSTLZH/OKuWqGzEEkLX2cHx8XbwfDY7bHyCHQKfbIfMp9qh8hG1ggphbwXPKFghMDqscDBqVmiMZLHBIpQ3G2ycDhscnJoNGifxFvBCOlogsqoWqKxk4bcI7cgv8qr8Ki9Z2a1CPLFLzES5qVDfue3cKrfKTTZmm5AvzAo78by80M+8Mj/xrLwYMLPKFpCN0yYmrMpIqw0kMApixOb4uGYFOfgcYseVzSqGbMpFmyUkc8liy5XJKsZsC4+lWUMKjyL2TCySWHTjsIlNFwaLGLX58flmFWludK6JXYuOTYtYNunI9BDbHiM7xLppXIfYN43qEAunIWkSGx8D0ixWLjoaLWLnsoxlqWLp5keyNjH2Po4o9j50DC6LxZsfwdrE6EmfzWWxe/PPtTcxffbPs1Uxf16ew2eZwuR+ns8yjXn9UbpXmcoal5+yJpnQGpffTdfUZFbbEfzD1Ics01tzCN77Xy3eh5ibTHT81SqzXXDnMVvLPdrmasfd61RldKaJaq5H6zyt6PbTdOCBcZKqPkLrJCXX59Ikicix6z26Z5nrEsNvxiL/+/+PSAA="
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsBodyParts() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Body",
                            "The physical structure, including the bones, flesh, and organs, of a person or an animal.",
                            "https://www.anglomaniacy.pl/img/xv-body.png.pagespeed.ic.VrPozNS9ng.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Ear",
                            "The part of the body specialized for the perception of sound; it is an organ of hearing.",
                            "https://www.anglomaniacy.pl/img/xv-ear.png.pagespeed.ic.CU4NdOtmku.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Eye",
                            "The organ of sight in humans and animals. It makes us see different objects.",
                            "https://www.anglomaniacy.pl/img/xv-eye.png.pagespeed.ic.sVV10Iwtur.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Feet",
                            "The lower part of the leg below the ankle, on which a person stands or walks.",
                            "https://www.anglomaniacy.pl/img/xv-feet.png.pagespeed.ic.SmyAGevmwL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hair",
                            "Hair is the strands of fiber that grow from a person's skin, especially on the scalp.",
                            "https://www.anglomaniacy.pl/img/xv-hair.png.pagespeed.ic.j6ABDqdL88.webp"
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsBodyParts1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hand",
                            "The part on the end of the human arm. It is used for grasping or holding.",
                            "https://www.anglomaniacy.pl/img/xv-hand.png.pagespeed.ic.Zyu2wRzUx5.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Head",
                            "The part of the body containing the brain, eyes, ears, nose, and mouth.",
                            "https://www.anglomaniacy.pl/img/xv-head.png.pagespeed.ic.7xGSG8hkbe.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Legs",
                            "One of the long body parts that are used especially for standing, walking, and running.",
                            "https://www.anglomaniacy.pl/img/xv-legs.png.pagespeed.ic.jR0pKfqOXo.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Mouth",
                            "The opening in the face through which one eats, breathes, and speaks.",
                            "https://www.anglomaniacy.pl/img/xv-mouth.png.pagespeed.ic.jTSXoYm7fL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Neck",
                            "The part of the body that connects the head with the body.",
                            "https://www.anglomaniacy.pl/img/xv-neck.png.pagespeed.ic.mtxwbT_Sro.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Nose",
                            "The part of the face through which we breathe and smell.",
                            "https://www.anglomaniacy.pl/img/xv-nose.png.pagespeed.ic.5TFaMnM-uT.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tooth",
                            "One of the hard, white, bony objects that grow in rows in the jaws of our mouth. Teeth are used for biting and chewing.",
                            "https://www.anglomaniacy.pl/img/xv-tooth.png.pagespeed.ic.GQ0Yf8YDry.webp"
                    )
            );

            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsBathroom() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bathtub",
                            "A large tub in which a person can wash.",
                            "https://www.anglomaniacy.pl/img/xv-bath.png.pagespeed.ic.uB6miierqO.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Brush",
                            "A tool made of stiff hairs or bristles that have been fastened to a handle. A brush is used for grooming, or scrubbing.",
                            "https://www.anglomaniacy.pl/img/xv-brush.png.pagespeed.ic.0X3s6eyE6w.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bath Mat",
                            "A mat or washable rug used to stand on when entering or leaving a bath.",
                            "https://thumbs.dreamstime.com/b/bath-mat-clip-art-illustration-vector-isolated-bath-mat-clip-art-illustration-vector-isolated-white-background-198036649.jpg"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Comb",
                            "A flat device with narrow pointed teeth. It is used to smooth, arrange, or hold hair in place.",
                            "https://www.anglomaniacy.pl/img/xv-comb.png.pagespeed.ic.4TNQjOLNjJ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Detergent",
                            "A chemical which is in form of liquid, powder that people use for washing things, mainly clothes",
                            "https://momlovesbest.com/wp-content/uploads/2017/09/regular-or-high-efficiency.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Mirror",
                            "A smooth surface that reflects an image of whatever is in front of it. ",
                            "https://www.anglomaniacy.pl/img/xv-mirror.png.pagespeed.ic.wR4A4ITISX.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shampoo",
                            "A soap solution used to clean the hair.",
                            "https://www.anglomaniacy.pl/img/xv-shampoo.png.pagespeed.ic.waMOpaxyIy.webp"
                    )
            );


            return vocabularyDetail;
        }

        private static ArrayList<VocabularyDetails> getVocabularyDetailsBathroom1() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sink",
                            "A fixed basin connected to a water supply and a drain. It is used to wash hands and face.",
                            "https://www.anglomaniacy.pl/img/xv-sink.png.pagespeed.ic.dhenp5BkdL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Soap",
                            "A cleaning substance that is made from fat or oil and comes in the form of a bar, liquid, powder, or flakes. It is used to rub and clean the body.",
                            "https://www.anglomaniacy.pl/img/xv-soap.png.pagespeed.ic.Zc7hc7vq7v.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sponge",
                            "An artificial material with many small holes that can soak up water easily. It is used for cleaning or washing.",
                            "https://www.anglomaniacy.pl/img/xv-sponge.png.pagespeed.ic.nxZGXvThZd.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Toothbrush",
                            "A brush with a short, narrow head and a long handle used to clean the teeth.",
                            "https://www.anglomaniacy.pl/img/xv-toothbrush.png.pagespeed.ic.oCrKJFYsF4.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Towel",
                            "A piece or length of soft cloth used to wipe or dry the face and body.",
                            "https://www.anglomaniacy.pl/img/xv-towel.png.pagespeed.ic.lJWcXp8ukA.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tap",
                            "A device to control the flow of liquid or gas from a pipe. In bathroom, we draw water from it.",
                            "data:image/jpeg;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Washing machine",
                            "A machine that washes laundry or clothes.",
                            "https://img.freepik.com/premium-vector/happy-cute-kid-laundry-with-washing-machine_97632-3732.jpg?w=2000"
                    )
            );

            return vocabularyDetail;
        }


        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsSchool() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Board",
                            "Where the teacher writes.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Book",
                            "What you read.",
                            "https://www.anglomaniacy.pl/img/xv-book.png.pagespeed.ic.020mEqmDn0.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Calculator",
                            "For making mathematical calculations.",
                            "https://www.anglomaniacy.pl/img/xv-calculator.png.pagespeed.ic.mAZNKr-axi.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Classroom",
                            "A place where you study.",
                            "https://www.anglomaniacy.pl/img/xv-classroom.png.pagespeed.ic.bwTyoCJ4kK.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsSummer() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Ball",
                            "Large and light, filled with air, made for use at the beach.",
                            "https://www.anglomaniacy.pl/img/xv-ball-s.png.pagespeed.ic.IygdLttrEz.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Carb",
                            "A sea creature with two large claws nad eight legs that walks sideways.",
                            "https://www.anglomaniacy.pl/img/a-crab.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hat",
                            "Shades the face and neck from the sun.",
                            "https://www.anglomaniacy.pl/img/xv-hat-s.png.pagespeed.ic.LQRYzUzzom.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hot",
                            "Very warm.",
                            "https://www.anglomaniacy.pl/img/xv-hot.png.pagespeed.ic.VFHqBVqrzX.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsTown() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bank",
                            "A public place where money is kept.",
                            "https://www.anglomaniacy.pl/img/xv-bank.png.pagespeed.ic.GzkoJmQEsr.webp"
                    )
            );

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Church",
                            "A place for worship.",
                            "https://www.anglomaniacy.pl/img/xv-church.png.pagespeed.ic.ECYb-SitpB.webp"
                    )
            );

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cinema",
                            "A place where films are shown.",
                            "https://www.anglomaniacy.pl/img/xv-cinema.png.pagespeed.ic.p7G-OsnRM1.webp"
                    )
            );

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Gas Station",
                            "A place for servicing vehicles with gasoline and oil.",
                            "data:image/webp;base64,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"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsTransport() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bike",
                            "Has two wheels, a handle bar and pedals.",
                            "https://www.anglomaniacy.pl/img/v-bike.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Boat",
                            "A small vessel for travelling over water.",
                            "https://www.anglomaniacy.pl/img/xv-boat.png.pagespeed.ic.Rm9bVPyXqm.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bus",
                            "A large vehicle that takes many passengers to places.",
                            "https://www.anglomaniacy.pl/img/xv-bus.png.pagespeed.ic.OoUrTblUgL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Car",
                            "A motor vehicle with four wheels.",
                            "https://www.anglomaniacy.pl/img/xv-car.png.pagespeed.ic.Yk6kYF9bvG.webp"
                    )
            );
            return vocabularyDetail;
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsWeather() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cloud",
                            "A puffy thing that moves across the sky.",
                            "https://www.anglomaniacy.pl/img/xv-cloud.png.pagespeed.ic.H0bX6VrX8t.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cold",
                            "Feeling no warmth.",
                            "https://www.anglomaniacy.pl/img/xv-cold.png.pagespeed.ic.Rr8ILjOWoN.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Frost",
                            "Ice crystals formed on a cold surface.",
                            "https://www.anglomaniacy.pl/img/xv-frost.png.pagespeed.ic.TMoclCyate.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hail",
                            "Frozen, hard balls falling from clouds.",
                            "https://www.anglomaniacy.pl/img/xv-hail.png.pagespeed.ic.75K_eEBzxq.webp"
                    )
            );
            return vocabularyDetail;
        }
    }
}


