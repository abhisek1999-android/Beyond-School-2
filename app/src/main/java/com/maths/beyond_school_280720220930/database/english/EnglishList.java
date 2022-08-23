package com.maths.beyond_school_280720220930.database.english;

import androidx.annotation.NonNull;

import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyModel;

import java.util.ArrayList;

public abstract class EnglishList {


    protected abstract static class GradeOneVocabulary {
        protected static EnglishModel englishListGrade1() {

            ArrayList<VocabularyDetails> vocabularyDetailBathroom = getVocabularyDetailsBathroom();
            ArrayList<VocabularyDetails> vocabularyDetailBody = getVocabularyDetailsBodyParts();
            ArrayList<VocabularyDetails> vocabularyDetailColor = getVocabularyDetailsColors();
            ArrayList<VocabularyDetails> vocabularyDetailAnimals = getVocabularyDetailsAnimals();
            ArrayList<VocabularyDetails> vocabularyDetailFruits = getVocabularyDetailsFruits();
            ArrayList<VocabularyDetails> vocabularyDetailVegetables = getVocabularyDetailsVegetables();


            var listVocabulary = new ArrayList<VocabularyModel>();
            listVocabulary.add(
                    new VocabularyModel(
                            "bathroom",
                            vocabularyDetailBathroom
                    )
            );
            listVocabulary.add(
                    new VocabularyModel(
                            "body_parts",
                            vocabularyDetailBody
                    )
            );
            listVocabulary.add(
                    new VocabularyModel(
                            "colors",
                            vocabularyDetailColor
                    )
            );
            listVocabulary.add(
                    new VocabularyModel(
                            "animals",
                            vocabularyDetailAnimals
                    )
            );
            listVocabulary.add(
                    new VocabularyModel(
                            "fruits",
                            vocabularyDetailFruits
                    )
            );
            listVocabulary.add(
                    new VocabularyModel(
                            "vegetables",
                            vocabularyDetailVegetables
                    )
            );

            return new EnglishModel(
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
//
        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsVegetables() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cabbage",
                            "Has a round firm head of leaves.",
                            "https://www.anglomaniacy.pl/img/xv-cabbage.png.pagespeed.ic.UZO89Qu5NY.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Carrot",
                            "Orange, grows underground.",
                            "https://www.anglomaniacy.pl/img/xa-carrot.png.pagespeed.ic.UlnbqiVWK3.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cauliflower",
                            "Has a white centre surrounded by green leaves.",
                            "https://www.anglomaniacy.pl/img/xv-cauliflower.png.pagespeed.ic.aalIHOEd49.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Celery",
                            "Has a white centre surrounded by green leaves.",
                            "https://www.anglomaniacy.pl/img/xv-celery.png.pagespeed.ic.3nJ9FnXlQ-.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cucumber",
                            "Green on the outside, white on the inside.",
                            "https://www.anglomaniacy.pl/img/xv-cucumber.png.pagespeed.ic.50uPDxIdE9.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Leek",
                            "Has mildly sharp-tasting leaves and thick stalk.",
                            "https://www.anglomaniacy.pl/img/xv-leek.png.pagespeed.ic.vZ3RVknXS2.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Lettuce",
                            "Green and leafy, tastes good in salads.",
                            "https://www.anglomaniacy.pl/img/v-lettuce.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Onion",
                            "A sharp-tasting vegetable.",
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
                            "Tiny, round and green, grow in pods.",
                            "https://www.anglomaniacy.pl/img/xv-peas.png.pagespeed.ic.zqr6iziFXs.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Potato",
                            "Grows underground, has white flesh with brown skin.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tomato",
                            "Soft and red",
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
                            "White on the inside, red, yellow or green on the outside.",
                            "https://www.anglomaniacy.pl/img/xa-apple.png.pagespeed.ic.0RezGjCEt9.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Banana",
                            "A long, thin fruit.",
                            "https://www.anglomaniacy.pl/img/xv-banana.png.pagespeed.ic.Y3sEGwwf2J.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cherry",
                            "A sweet fruit that starts with C.",
                            "https://www.anglomaniacy.pl/img/a-cherry.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Grapes",
                            "Green or purple berries which grow in bunches.",
                            "https://www.anglomaniacy.pl/img/xa-grapes.png.pagespeed.ic.tvj0f-cSPl.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Lemon",
                            "A sour, yellow fruit.",
                            "https://www.anglomaniacy.pl/img/xv-lemon.png.pagespeed.ic.k3wkdO_efN.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Orange",
                            "A round orange or yellow fruit.",
                            "https://www.anglomaniacy.pl/img/xa-orange.png.pagespeed.ic.YIZoYKL7lG.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Peach",
                            "A soft, slightly furry fruit with sweet yellow flesh.",
                            "https://www.anglomaniacy.pl/img/xa-peach.png.pagespeed.ic.NpEgV4bTtE.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pear",
                            "A green or yellow fruit with juicy sweet flesh.",
                            "https://www.anglomaniacy.pl/img/xa-pear.png.pagespeed.ic.3C9z6q6420.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pineapple",
                            "Sweet, yellow-fleshed, with a pinecone shape.",
                            "https://www.anglomaniacy.pl/img/xa-pineapple.png.pagespeed.ic.2oIW73lYRL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Plum",
                            "A round or oval fruit, usually red or purple.",
                            "https://www.anglomaniacy.pl/img/xv-plum.png.pagespeed.ic.Asvtk4uq7i.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Strawberry",
                            "A sweet, red fruit that starts with S.",
                            "https://www.anglomaniacy.pl/img/xv-strawberry.png.pagespeed.ic.TZbAiPq5KZ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Watermelon",
                            "Green on the outside and pink on the inside.",
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
                            "Likes eating mice",
                            "https://www.anglomaniacy.pl/img/xh-kitten.png.pagespeed.ic.R5H4s8fnem.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cow",
                            "Give us milk",
                            "https://www.anglomaniacy.pl/img/xa-cow.png.pagespeed.ic.WsYtPWR47_.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Donkey",
                            "Patient but stubborn.",
                            "https://www.anglomaniacy.pl/img/xa-donkey.png.pagespeed.ic.LLTkbD1JrI.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Duck",
                            "Makes a quacking sound.",
                            "https://www.anglomaniacy.pl/img/xa-duck.png.pagespeed.ic.ziCtSB021B.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Goat",
                            "Has hollow horns and eats almost anything.",
                            "https://www.anglomaniacy.pl/img/xa-goat.png.pagespeed.ic.YBK3Svr3YE.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Goose",
                            "A long-necked bird.",
                            "https://www.anglomaniacy.pl/img/xa-goose.png.pagespeed.ic.Owp7nto9C2.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hen",
                            "Gives us eggs.",
                            "https://www.anglomaniacy.pl/img/xa-hen.png.pagespeed.ic.N08juYANpX.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Horse",
                            "Strong, can run very fast.",
                            "https://www.anglomaniacy.pl/img/xa-horse.png.pagespeed.ic.LHJmdvDwVc.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pig",
                            "Likes to roll around in mud.",
                            "https://www.anglomaniacy.pl/img/xa-pig.png.pagespeed.ic.zAUOKKFLR5.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Rabbit",
                            "Has long ears.",
                            "https://www.anglomaniacy.pl/img/xa-rabbit.png.pagespeed.ic.nzJRWp5o7M.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sheep",
                            "Has woolly fur.",
                            "https://www.anglomaniacy.pl/img/xa-sheep.png.pagespeed.ic.a-klSoAUqn.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Turkey",
                            "Has a snood and can gobble.",
                            "https://www.anglomaniacy.pl/img/xa-turkey.png.pagespeed.ic.iTI11qpBPV.webp"
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
                            "Penguins are white and black.",
                            "data:image/webp;base64,UklGRgAEAABXRUJQVlA4TPMDAAAv+YA1EP+gqG0jpgx683X8kU4KCtq2YVNl0O4fBW3bsKkyaPePQCCFGXwCACAAfySJRCQiqX9ZRSSUiERIImlQsG27bVs9FIIAiHLduzX/cWZFUCTch+TXiP5PgPzv/9+P1p9rV8lsMZeO0V5y3MzSmJArfmXNwayJTQ3X2JJdDRMqrrcGsxA+dVx3T34RfMEtFr8AvuBWi1OeiR2326PR3NZw221Tmy24/WJ1Fjpm2IPCTMIsk9GWq5hndbpyHTPtTlM7ZrvrKWK+UUsZM846yphz1tCBWR/6yZh31k7EzKNuEuaeNBMw+6AXh/k7rZhKoBqlZDDMOtnBcdeIA0unD9NodKOOAzwPbQQw3XVhO5VuVXGAa9HEBra7Hkyj060aIvgeWrBg7JVQKBUdeHAOKiikmgY8WAcFFADPL4N3HBo/D+DpNPgOkoHeAeBj4PueRWPnADycBl9AM5DLAN4Gvu94FG6mA3ffAy8gaqkFAE+n87/vmCRqDcDnwBuYdkPMAbg/DT5QQSCWALwMfILrQawB+Bp4JgNLywF4OA3es9lpJQAvA59gW2hVAJ8Dr3RgSFkA96fBRz4bqR3A0wj4ZlIHgLeBD0KNVAfwPfBKCI6SA3B/GnxiFCjtAJ5GHhllShnA2wgYN0oNwNfAFyUYRgDuToMfnDwhD+Bx5I1TJLQDeBl54ZQJJQDvKqiECoDPkSdOINQAnEYfSVk+0IOnYxUR6HhFRDqbIhKdeJF7UkUFWKR0kUdSlU5RBP468zh6t1Ja/JeorIhGJyqi/CVmU0Si4xURf6OJIvzvtKIH4XuooROKaiiEghoSIa+GnZCowTOqWjCMshKqMN6VkCk5JQRK0nXgOB0q6MJ5V0Em5VQQSEnTgGWVFFCF9aaAnZZ0fpZXpleFt6e3E5PGzjCL5A5hbsl5anJQa8LdUwvkpBHrhl0gFoV+42X47bSiKLCR6kYDgVQUFTZKzejAUwqixEKoihYtIa8GiXSy6NE0Mt0oQjwZL6pMVLIosxJpRhuOiBN17jSiKPQgUUSjplFoRiXiOoHuRKkbgU3UGqYXRLF5cllUW6dWRLllYkW0a+q0ilGPmDqpYkTBpk6pGFGxKRM6RMsmTyeLotNkoqg6zKRvomxXp1GtqNuWSSQjGg8z6Jso3ZWby0b0vvWbapuo3uTb6dGI9m2+kWhkBW3uV9eTlVW0qV9VC0aWMhzX0rOTBd1y+2UtbbKsbj/qxWrarCyvDzGX0n5WSoqbl4U2Z+yy2gd+3u1ibTj3WCvbz8K2VAXnd7NQO0aPdXIY35apXqCZRUq4ZFokny+Q/SKJuKOf1Q8nS+32o/x47E7+9/9fCwEA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Blue",
                            "The sky is blue.",
                            "data:image/webp;base64,UklGRhYEAABXRUJQVlA4TAoEAAAv+YA1EPegIJLU5sG/3A7pMaGmbSQp/FludU/lA3CSgCSx/5sDg2wjFeH96Q5hBk82QFEYHYJY/08UCglf2KWkkKhHIVGQCdJOlP7+J2Sc42wmULRtq23tHNu3t30QkgBR/v9D84xjsw/piej/BMj//v8Dslw9TTkGv6B78SHmucnBK35H9SFPSlpxl2uajrIp7la3MhEtOty5i20OWlA8oIZmvxYUD6qhGS85PLBLlsseD+6z2aLi4TXarHoM0VeDRcUgNZorYKDBVs1jqL4ZqioGq9VMRTFcLUYqGHIxUcKgk4EShp3MkzDwZJyMoWfTHBj8YZiso9NsluYwfNessoHgZpQEiskkGSSzQZpj4Zo9PGh6c0QQjcbIykSzLRZQXUwRQTYaoiobrXbwoOvNUEC4WMExckYIoBxM0JSTNgsEkA4GqKBd+e0Afr46f0js9CoAnE/Xn1mgsgsA3k+db2AZyDUFcOm4gKY2bgnA56nzgwcSNwfgpeMCoo5aBvBz6vxggsxsA/DacQbVjVhTAJeOVy7aeCUAn6fOHy5IvFYALx1PILvSagBw7nhng8YqAfg8df7QSaxWAC8dF9BdWSmAc8crHyV1APg+dX7wQeG0A3jvAeGd0wLguePCaKHUAODU+cIIjdEB4KvnjdLBKAB46/mkFBh5AM8935Q8IwVw7gFlJVQBfJ8sgMqnAPjoeSZV+AQAbzYIfDYAzz0vpDY+HsC5542U56MATjZQPrAD6GRLZDblJh+sCpvjJl+sDjbBEuGvMP4mb6zWifOUnns/ZsqK//kgWCL8FSZaIrIplih/nFVLVDZiCaHr7OD4eDt4PpsdNj7BDoFPsUPhU+1Q+YhaQYWwt4JnFKwQGB1WOBg1KzRGsthgEcqbDTZOhw0OTs0GjZN4C3ghHS0QWVULVFay8FuEduQXeVV+lZes7FYhntglZqLcVKjv3HZulVvlJhuzTchnZpmdeF5e6BdehZ94Vl4MWFgVC8jGaRMTVmWk1QYSGAUxYnN8XLOCHHwOsePKZhVDNuWizRJSuBSx5cpkFWO2hcfSrCGZRxZ7JhZJLLpx2MSmC4NFjNr8+HyzijQ3OtfErlnHplksm3Rkeohtj5EdYt00rkPsm0Z1iIXTkDSJjY8BaRErZx2NZrFzXsayVLF08yNZmxh7H0cUex86BlfE4s2PYG1i9KSP5orYvfnH2puYvvjH2aqYvyyP4YtMYXL354tMY1nvSvcqU1njci9rkgmtcfnddE1NZrUdwd9MfSgyvbWE4L3/1eJ9iKXJRMdfrTLbGVces7Vco22udly9TlVBZ5qo5nq0ztOKbj9NB24YJ6nqLbROUnJ9Lk2SiBy7XqN7kbnOMfxmzPK///+IBA=="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Brown",
                            "The wood is brown.",
                            "data:image/webp;base64,UklGRhQEAABXRUJQVlA4TAgEAAAv+YA1EP+gkJEkqfwtz6BP4JcwGjAJ+P+txpEUg2wj1d/yDPYAnxhkG2kIRTiE95f7KpDW/UeViioVVa6omfpXKmrsItg8ikFhCIVRCEbxDhRs227bOk+Se73oJEE+zH+e+YJi4T6kJ6L/EyD/+/8PyHD1NPlc6o7uvZbs58aXqvgdtRY/KWnDj9zSdIRV8WN1DRPh8oEffmQ3B64o7lCLs58rijvV4oyXDtzxkSznK+68erNlxd1rtlmrGGJtBsuKQWo2V8FAi61cxVCrM1RTDFabmYJiuBqMFDDkYKKEQScDJQw7mSdh4Mk4HkP3pokYfDSM19GpN4s7MPzDWWUFwdUoCRSTSTxIeoO4g8Xh7FFBs5ojg2g2hlcm6m2xg+puigyy2RBN2WizQwXdaoYAwsEKB6PDCAWUiwmcclJngQLSxQANtBu/BcD3Z+c3iYVeA4Dz6fozCzR2BcDbqfMVLAs5pwAuHRfQVMctAfg4db7zQOJ2AHjquIDoQc0D+D51vjOBZ7YCeOk4g+pKzCmAS8cLF3W8EoCPU+c3FyReG4DnjgeQ3Wg5ADh3vLGBY5UAfJw6v+kkVhuA544L6G6sFMC544WPkooAvk6d73wQOC0A3npAeOG0A3jsuDDaKTkAOHU+MYJjFAF89rxSiowKgNeeD0qFUQXw2PNFqTJSAOceUFZCDcDXyQJofAKA955HUoFPAfBqg8JnBfDY80xq5VMBnHteSVU+CuBkA+UDO4COt4RnE27yziqwiTf5ZBXZFEuUv8LUm7yy2iauUnrsfZ8pK/7ng2KJ8leYbInMJlgi/HHWLNHYiCWE7mGHg0+1Q+Wz2mHlU+xQ+AQ7BD7NDo2PqBVUCFcrVEbFCoVRtEJk5KzgGMlug10orzZYOUUbRE7OBo6TVAtUIZ0tkFk1CzRWsvPbhXbml3k1fo2XbOw2IZ7YJWai3FSoL9wWbo1b4yYrs1XIe2aenVReVegHXoGfVFZVDBhYBQvIymkVEzZlpM0GUhgVMaI7+BzOChL5RLHjxmYTQzrlos4SErgEseXGZBNjup3H7qwhnocXeyYWSSy6cljFpjuDXYzq6viqs4q4Y3SHE7t6HZt6sWzSkWkU28aRRbFuGlcU+6ZRRbFwGpImsXEckAaxstfRqBc7+30sexNLuzqSzYmxl3FksXfUMRxBLO7qCDYnRk96b0cQu7t6X4sT04d6P2sT84f9PmqQKUzHz6tBpjFsP0qXJlPZ8v5TtiQT2vL+u+mWnMyqi6XeTGsJMr0tlFJr/dVea8nByUTnX20y2x5Xxtnar1E3Vwuu3qYqoDNNlDt6tM3Thu46TRE3zJPU9BbaJikdfUeaJBGJi16jS5C59rn8Zvbyv///iAQ="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Green",
                            "The grass is green.",
                            "data:image/webp;base64,UklGRhoEAABXRUJQVlA4TA4EAAAv+YA1EP+gIJLU5sG/3A7pMWGQbaT6W77BDuATg2wj1d/yDXYAnxhkG6kI7093CDN4tWkbME7ZkkOA9f9ZqlTJUqU2f1ebKlmqyB3QJqLf/wmMOWaTQMG2rbix82xX396HkASIZv7zzDdOmftIn4j+T4D87/8/IMvV05Rj8Au6Fx9inpscvOJ3VB/ypKQVP3JN01E2xY/VrUxEiw4/3MU2By0o7lBDs18LijvV0IyXHO7YJctljzv32WxRcfcabVY9huirwaJikBrNFTDQYKvmMVTfDFUVg9VqpqIYrhYjFQy5mChh0MlACcNO5kkYeDJOxtCzaQ4M/jBM1tFpNktzGL5rVtlAcDNKAsVkkgyS2SDNsXDNHh40vTkiiEZjZGWi2RYLqC6miCAbDVGVjVY7eND1ZiggXKzgGDkjBFAOJmjKSZsFAkgHA1TQrvx2AN+fnd8kdnoVAM6n688sUNkFAG+nzlewDOSaArh0XEBTG7cE4OPU+c4DiZsD8NRxAVFHLQP4PnW+M0FmtgF46TiD6kasKYBLxwsXbbwSgI9T5zcXJF4rgOeOB5BdaTUAOHe8sUFjlQB8nDq/6SRWK4DnjgvorqwUwLnjhY+SOgB8nTrf+aBw2gG89YDwzmkB8NhxYbRQagBw6nxihMboAPDZ80rpYBQAvPZ8UAqMPIDHni9KnpECOPeAshKqAL5OFkDlUwC89zySKnwCgFcbBD4bgMeeZ1IbHw/g3PNKyvNRACcbKB/YAXSyJTKbcpN3VoXNcZNPVgebYInwVxh/k1dW68R5So+97zNlxf98ECwR/goTLRHZFEuUP86qJSobsYTQdXZwfLwdPJ/NDhufYIfAp9ih8Kl2qHxEraBC2FvBMwpWCIwOKxyMmhUaI1lssAjlzQYbp8MGB6dmg8ZJvAW8kI4WiKyqBSorWfgtQjvyi7wqv8pLVnarEE/sEjNRbirUd247t8qtcpON2SbkM7PMTjwvL/QLr8JPPCsvBiysigVk47SJCasy0moDCYyCGLE5Pq5ZQQ4+h9hxZbOKIZty0WYJKVyK2HJlsoox28JjadaQzCOLPROLJBbdOGxi04XBIkZtfny+WUWaG51rYtesY9Mslk06Mj3EtsfIDrFuGtch9k2jOsTCaUiaxMbHgLSIlbOORrPYOS9jWapYuvmRrE2MvY8jir0PHYMrYvHmR7A2MXrSe3NF7N78fe1NTF/8/WxVzF+W+/BFpjC5n+eLTGNZf5TuVaayxuWnrEkmtMbld9M1NZnVdgR/M/WhyPTWEoL3/leL9yGWJhMdf7XKbGdceczWco22udpx9TpVBZ1poprr0TpPK7r9NB24YZykqrfQOknJ9bk0SSJy7HqN7kXmOsfwmzHL//7/IxI="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Grey",
                            "Made by mixing black and white",
                            "data:image/webp;base64,UklGRjAEAABXRUJQVlA4TCQEAAAv+YA1EPegqJEkpXf9W4Xj+7EJxW3bOEr2n7Veex2suG0bR8n+s9Zrr4MRM5KkQhik838NwiCswRFM0lTbMYD1/x/3mu8Z95rvyd3KjPme8Z55r/8WAQjQLR1wwMQswIADtqUA3VK2VHWvuj9QtG2rbe0c27e3DUISRRz+/0PzjGOzD+mJ6P8EyP/+/wOyXD1NLcewoHsJMbe5aTEofkcNsU1K2nCXW5qOsiruVtcyES573LnPbg5cVDygRmc/FxUPqtEZL3k8sE+WawEPHprZsuLhNdusBgwxVINlxSA1mytioNFWLmCowRmqKgar1UxFMVwtRioYcjFRwqCTgRKGncyTMPBknIahN9McGPxhmKaj02YW5zF876yyguBqlASKySQNJJtBnGfhnT0CaAZzZBDNxmjKRJstFlBdTJFBNhuiKhutdgigG8xQQLhYwTPyRoigHE3glJM6C0SQjgaooF357QB+vjp/SOz0KgCcT9efWaCyiwDeT51vYBnJOQVw6biApjpuCcDnqfODBxI3D+Cl4wKinloD8HPq/GCCxmwF8NpxBtWVmFMAl45XLup4JQCfp84fLki8NgAvHU8gu9FyAHDueGcDxyoB+Dx1/tBJrDYALx0X0N1YKYBzxysfJXUA+D51fvBB4bQDeO8B4Z3TAuC548JooeQA4NT5wgiO0QHgq+eN0sEoAnjr+aQUGQUAzz3flAIjBXDuAWUlVAF8nyyAyqcA+Oh5JlX4RABvNoh8VgDPPS+kVj4BwLnnjVTgowBONlA+sAPoNEs0NuUmH6wKm+MmX6wONtES8a8w4SZvrLaJC5Seez9myor/+SBaIv4VJlsisymWKH+cVUtUNmIJoevt4PkEOwQ+qx1WPtEOkU+xQ+FT7VD5iFpBhXCwQmAUrRAZHVY4GDkrOEay2GARyqsNVk6HDQ5OzgaOkwQLBCGdLZBZVQtUVrLwW4R25pd5VX6Vl2zsNiGe2CVmotxUqO/cdm6VW+UmK7NVyDdmjZ0EXkHoF16FnwRWQQxYWBULyMppFRNWZaTVBhIZRTGi83y8s4IcfA6x48ZmE0M65aLOElK4FLHlxmQTY7qFx+KsIY1HE3smFkksunJYxaYLg0WM6sL4grOKOD8678SuTcemTSybdGR6iG2PkR1i3TSuQ+ybRnWIhdOQNImNjwFpESs3HY02sXNbxrJUsbQLI9mcGHsfRxZ7HzoGX8TiLoxgc2L0pI/mi9jdhcfanZi+hMdZq5i/LI8Rikxh8vcXikxj2e5K9ypTWfNyL1uSCa15+d10S05m1R0x3ExDLDK9tcQYQvjVEkLMxclE519tMtsNVx6ztVyjbq52XL1NVUFnmijne7TO04buME0HbpgnqeottE5S8n0+TZKIHLteo3uRuW45/mZu8r///4gE"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Orange",
                            "Oranges are orange",
                            "data:image/webp;base64,UklGRhgEAABXRUJQVlA4TAsEAAAv+YA1EPegJgDaBurf6h7wBRsCksT+bw4YtZHkyPxZbvpdqANwkkG2kYowhEN4f7kPoP5fYpFYJPaRuCzHdbFILL0BRFYUahQigUKkEIgUKg4UZNuO20ZfsjN5egAIQCTIj/0vNDEcC++x547o/wTY//7/AzK9+zTFUtuO4b3VEs9NrM3xG73VeFLyhk+55dORVsen9TWdiFAOfPKjhHMQquMOvQb9QnXcqdcgXj5wx0dWLjbceYuyFcfde9GsN0yxdcGKY5Je5KqYaNUqNEy1BaG6Y7LeZUqO6XoSKWHKSaKMSWeBMqad5cmYeBYnYupRmgWTX4SJPjuPsoQD0z+CKisIrqJkUMySRJCMgoSDxRH0aKDZ5CggWsSIzsSjFjuo7lIUkC1CdGfjXYcGuk2GBMJJhYPRIUIF5SpBcE4eFKggXQXooN353QC8vQy+kbjR6wBwvbz/ygKdXQXwdBn8AZaVXHAADwMPoOmBWwbwfBl84oHM7QDwdeABRA9qEcDbZfCJCSKzFcD3gSuorsSCA3gY+M7FA68M4Pky+MYFmdcG4NvAI8hutAIAXAee2CCwygCeL4NvdDKrDcC3gQfQ3Vg5gOvAdz5OagHwehl84oPE6QbgaQSEb5x2AF8GHhjtlAIAXAa/MkJgtAB4GflBaWFUAfwYeaZUGTUAX0ZeKTVGDuA6AspOqAN4vSiAzicBeBr5QirxqQB+aFD5rAC+jHwjtfJpAK4jP0g1Pg7gooHzgQ6gE5WIbNKHPLFKbJYPeWG1sKlK1L/CtA/5wWo7cY3Sl9GnM6Xifz6oStS/whQlCpukRPrjrCvR2ZgSRvfQ4eDTdGh8Vh1WPlWHyifpkPh0HTofcxXcCDcVGqOqQmW0qLAwCioERrZrsBvlVYOV06LBwiloEDhZU6AZ6aJAYdUV6Kxs57cb7cKv8Or8Oi/b2G1GPLPLzMy5uVG/cbtx69w6N1uZrUY+MovsrPFqRj/xSvyssWomYGKVFLCV02oSdmfkXQOrjKqJGA4+R1DBFj6L6bix2UzI4Fw8KGGJSzItNyabiRl2HntQwyKPaHpmFtkUXTmspunOYDdRQ5tfC6pYOGZ3BNM1+tw8mrLZZ+aLabvMbDF187wW0zfPajGF85Q8m8bLhDyZytFn49F0jvtc9m5KhzaTLZjYt3kU03vxORzJFA9tBlsw0bPf25FM99Du6xZM+tTuZ+0mf9rvoyU7hfn4fC3ZaUzbp/Jbt1PZy/5ZtmwntJf9t/mWg53VsNT2Yd5qstPbU62ttV/trdWSgp3o8qvNznbEO5eztb/Hw7m64d3bqUoYzCcqHCPez9OG4XaaFnxgOUndP8L7ScrH2JFPkpktN3+P35Kd61jqT0u0//3/RyQA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Pink",
                            "Made by mixing red and white",
                            "data:image/webp;base64,UklGRvoDAABXRUJQVlA4TO0DAAAv+YA1EO+gKJKkZv3LvEwGA7ySBQJJG9e/808gaeP6d/4FAkn+tBucAP5Bv7010UFwJKwycYCCbdttW+fdB4IkAKKouXv+E80XFAv3IT0R/Z8A+d//f0Dmu6epxOAXdC8+xDI3JXjF76g+lElJK37kmqYjb4ofq1ueiBYdfriLbQ5aUDyhhma/FhRPqqEZLzk8sUuWKx5P7ovZouLpNdqsegzRV4NFxSA1mitgoMFWzWOovhmqKgar1UxZMVzNRsoYcjZRwqCTgRKGncyTMPBknIKhF9McGPxhmKKj02KW5jB816yygeBmlASKySQFJItBmmPhmj08aHpzRBCNxijKRIstFlBdTBFBNhqiKhutdvCg682QQThbwTFyRgigHEzQlJM2CwSQDgaooF357QC+Pzu/Sez0KgCcT/efWaCyCwDeTp2vYBnINQVw6biApjZuCcDHqfOdBxI3B+DWcQFRR60A+D51vjNBYbYBeOk4g+pGrCmAS8cLF228EoCPU+c3FyReK4BbxwVkV1oNAM4db2zQWCUAH6fObzqJ1Qrg1nEB3ZWVAjh3vPBRUgeAr1PnOx9kTjuAtx4Q3jktAK4dF0YLpQYAp84bIzRGB4DPnldKB6MA4LXng1Jg5AFce74oeUYK4NwDykqoAvg6WQCVTwbw3nMllfkEAK82CHw2ANeeG6mNjwdw7nkl5fkogJMNlA/sADrFEoVNfsg7q8zmeMgnq4NNsET4K4x/yCurdeI8pWvv+0xZ8T8fBEuEv8JES0Q22RL5j7NqicpGLCF0nR0cH28Hz2ezw8Yn2CHwyXbIfKodKh9RK6gQ9lbwjIIVAqPDCgejZoXGSBYbLEJ5s8HG6bDBwanZoHESbwEvpKMFIqtqgcpKFn6L0I78Iq/Kr/KSld0qxBO7xEyUmwr1ndvOrXKr3GRjtgn5wqywE8/LC/3MK/MTz8qLATOrbAHZOG1iwqqMtNpAAqMgRmyOj2tWkIPPIXZc2axiyKZctFlCMpcstlyZrGLMtvBYmjWk8Chiz8QiiUU3DpvYdGGwiFGbH59vVpHmRuea2LXo2LSIZZOOTA+x7TGyQ6ybxnWIfdOoDrFwGpImsfExIM1i5aKj0SJ2LstYliqWbn4kaxNj7+OIYu9Dx+CyWLz5EaxNjJ702VwWuzf/XHsT02f/PFsV8+flOXyWKUzu5/ks05jXH6V7lamscfkpa5IJrXH53XRNTWa1HcE/TH3IMr01h+C9/9XifYi5yUTHX60y2wV3HrO13KNtrnbcvU5VRmeaqOZ6tM7Tim4/TQceGCep6iO0TlJyfS5Nkogcu96je5a5LjH8Zizyv///iAQA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Purple",
                            "Made by mixing red and blue.",
                            "data:image/webp;base64,UklGRhQEAABXRUJQVlA4TAcEAAAv+YA1EPeApI0k1b/LY3dsFDx6EQrbtm34/8PZrcK2bRv+/3B2a5BtpCEcQhHeX+7LgN7dV1Xd1V3dVdVGdBF9qe4mgvf4H2AAgGH+B4ADAEXbttvUzgFubyq2z1ax/P8fmocIaB+lJ6L/EyD/+/8PSH/1NOUa24burcWa5ybHpvgdtcU8KWHFXa5hOvyuuFvd/USkeuDOj5rmIEXFA2pM9ktR8aAak/HCgQc+guVyw4O3bLaqeHitNnMNQ2zOYFUxSK3mihhotFVqGGpLhnKKwaozk1cMV72RPIbsTRQw6GCggGEH8wQMPBgnY+jZNAWDL4bJOjrNZkkHhn8kq+wguBslgGIwSQbJbJB0sDiSPRpoNnNUEK3GyMpEsy02UN1MUUG2GsIpG3V2aKDbzOBB2FvhYHQYIYJyNEFSTposEEE6GsCBtuO3APj56vwhsdBzAHA+XX9mAccuAng/db6BZSSXFMCl4wKamrgFAJ+nzg8eCNwOAC8dFxA9qGUAP6fODybIzHYArx1nUN2JJQVw6XjloolXAPB56vzhgsBrBfDa8QSyK60EAOeOdzZIrAKAz1PnD53AagXw2nEB3ZWVAjh3vPJRUgXA96nzgw88pwXAew8IL5w2AM8dF0YbpQQAp84XRkiMCoCvnjdKhVEE8NbzSSkyagCee74pNUYK4NwDykrIAfg+WQCOjwfw0fNMyvOJAN5sEPnsAJ57XkntfBqAc88bqcZHAZxsoHxgB9DJlshs/E0+WHk25SZfrAqbaIn4V5h2kzdW68Q1Ss+9HzNlxf98EC0R/wpTLVHZeEv4P86cJRwbsYTQPexw8Gl2aHx2O+x8oh0iH28Hz8fZwfERtYIK4WaFxihaITIqViiMkhUSI9lssAnl3QY7p2KDwinZIHGSZoEmpKsFKitnAcdKNn6b0K78Ki/Hz/GSld0qxAO7wEyUmwr1hdvCzXFz3GRntgv5zCyzk8arCX3Py/OTxqqJAT0rbwHZOe1iQqeM1NlAIqMoRkwHnyNZQQqfInZc2axiyKRcNFlCPBcvtlyZrGLMtPHYkjUk88hiz8AiiEV3DrvYdGOwiVFTG19LVpF0jO5IYtesY9Mslg06Mi1i2zKyItYN4ypi3zCqIhYOQ9IgNi4DUi9WzjoazWLnvI1lc2Lp1EayJjH2Mo4q9i46hsOLxVMbwZrE6EEf7fBi99Qea0liet8eZ3difr89RvMyheG4v+ZlGv16V7o4mUpXt3tZg0yoq9vvpmtIMqupxHYzbdHL9DofY2vtV1trsfokE11/tcpsZ1xZZmu7RtNcLbh6nSqPzjBR6ehRN08ruts0FdywTpLTW6ibpHD0HWGSRKQseo0uXuY61/ibNcv//v8jEgA="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Red",
                            "Something that is red is the colour of cherries or fire.",
                            "data:image/webp;base64,UklGRgAEAABXRUJQVlA4TPMDAAAv+YA1EPdgpm2b8ida7fMI7B0FAkkb17/zr6htG6j8ie54eQAmCQSS/Gk3OCPyHOm7fQAAVcbaAOAAACjattW2ds4+CEmAKLZvb///oXnGsdmH9ET0fwLkf///AZmvnqYSg1/QvfgQy9yU4BW/o/pQJiWtuMs1TUfeFHerW56IFh3u3MU2By0oHlBDs18LigfV0IyXHB7YJcsVjwf3xWxR8fAabVY9huirwaJikBrNFTDQYKvmMVTfDFUVg9VqpqwYrmYjZQw5myhh0MlACcNO5kkYeDJOwdCLaQ4M/jBM0dFpMUtzGL5rVtlAcDNKAsVkkgKSxSDNsXDNHh40vTkiiEZjFGWixRYLqC6miCAbDVGVjVY7eND1ZsggnK3gGDkjBFAOJmjKSZsFAkgHA1TQrvx2AD9fnT8kdnoVAM6n688sUNkFAO+nzjewDOSaArh0XEBTG7cE4PPU+cEDiZsD8NJxAVFHrQD4OXV+MEFhtgF47TiD6kasKYBLxysXbbwSgM9T5w8XJF4rgJeOJ5BdaTUAOHe8s0FjlQB8njp/6CRWK4CXjgvorqwUwLnjlY+SOgB8nzo/+CBz2gG894DwzmkB8NxxYbRQagBw6nxhhMboAPDV80bpYBQAvPV8UgqMPIDnnm9KnpECOPeAshKqAL5PFkDlkwF89DyTynwCgDcbBD4bgOeeF1IbHw/g3PNGyvNRACcbKB/YAXSKJQqbfJMPVpnNcZMvVgebYInwVxh/kzdW68R5Ss+9HzNlxf98ECwR/goTLRHZZEvkP86qJSobsYTQdXZwfLwdPJ/NDhufYIfAJ9sh86l2qHxEraBC2FvBMwpWCIwOKxyMmhUaI1lssAjlzQYbp8MGB6dmg8ZJvAW8kI4WiKyqBSorWfgtQjvyi7wqv8pLVnarEE/sEjNRbirUd247t8qtcpON2SbkC7PCTjwvL/Qzr8xPPCsvBsyssgVk47SJCasy0moDCYyCGLE5Pq5ZQQ4+h9hxZbOKIZty0WYJyVyy2HJlsoox28JjadaQwqOIPROLJBbdOGxi04XBIkZtfny+WUWaG51rYteiY9Milk06Mj3EtsfIDrFuGtch9k2jOsTCaUiaxMbHgDSLlYuORovYuSxjWapYuvmRrE2MvY8jir0PHYPLYvHmR7A2MXrSR3NZ7N78Y+1NTJ/942xVzJ+Xx/BZpjC5+/NZpjGvd6V7lamscbmXNcmE1rj8brqmJrPajuBvpj5kmd6aQ/De/2rxPsTcZKLjr1aZ7YIrj9lartE2VzuuXqcqozNNVHM9WudpRbefpgM3jJNU9RZaJym5PpcmSUSOXa/RPctclxh+Mxb53/9/RAIA"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Violet",
                            "Something that is violet is a bluish-purple colour.",
                            "data:image/webp;base64,UklGRhoEAABXRUJQVlA4TA4EAAAv+YA1EOegoI0kZe7Ov9VnRhsK27Zt1PH/t9lJKyBJ7P/mACEbSSrC+dM8yiCswQfw5t+CnjghCMnomSBkpyctaIGTgPsIxDeQLAcCA/3vPpJt+uxcs/ZA0bbtuG3zNHm+eARJzPz/L20EVcJ96NyI/k+A/O//PyDL3dNUU/ALuhcfUp2bGrzid1Qf6qTEDU+5xekoq+JpdS0T4dKBJz+SmwMXFC+owdnPBcWLanDGiwde+IiWqx4v7qvZkuLlNdmseQzRN4MlxSA1mStgoMFWzmOo3hmqKQarzUxFMVwtRioYcjFRxKCjgSKGHc0TMfBonIqhV9NkDD4bpurotJrFHRj+4ayyguBqlAiK0SQVJKtB3MHicPbwoOnNkUA0GaMqE622WEB1MUUC2WSIpmy02cGDrjdDAeFihYPRYYQAysEETjmps0AA6WCABtqN3w7g56vzh8ROrwHA+XT/mQUauwDg49T5DpaBnFMAl44LaKrjFgF8njo/eCByOwDcOi4gelCrAH5OnR9MUJmtAN46zqC6EnMK4NLxxkUdrwjg89T5wwWR1wbg1nEF2Y2WA4BzxwcbOFYRwOep84dOZLUBuHVcQHdjpQDOHW98lFQG8H3q/OCDwmkH8NHzQ2jntAC4dlxAeKHkAODUeWMExygD+Op5p5QZBQDvPZ+UAiMP4NrzTckzUgDnHlBWQg3A98kCaHwKgI+eK6nCJwB4t0HgswK49txIrXw8gHPPOynPRwGcbKB8YAfQqZaobMpDPlgVNvkhX6wym2CJ8FcY/5B3VtvEeUrX3o+ZsuJ/PgiWCH+FSZZIbIolyh9nzRKNjVhC6B52OPh4O3g+qx1WPsEOgU+xQ+HT7ND4iFpBhbC3gmcUrBAYZStkRs4KjpEsNliE8mqDlVO2QebkbOA4ibeAF9LJAolVs0BjJQu/RWgnfolX49d4ycZuE+KRXWQmyk2F+s5t59a4NW6yMluFfGVW2Ynn5YV+4VX4iWflxYCFVbGArJxWMWFTRtpsIIFRECO6g8/hrCCZTxY7bmw2MaRTLuosIYVLEVtuTDYxplt4LM4aUnlUsWdkEcWiK4dVbLowWMSozo/PO6uIO0Z3OLFr1bFpFctGHZlmsW0eWRbrxnFlsW8cVRYLxyFpFBvnAWkRK1cdjVaxc13GsjSxtPMj2ZwYex9HEntnHcNRxOLOj2BzYvSor3YUsbvzr7U7MX3xr7M2MX9ZXsMXmcJ4PJ8vMo1leyrdm0xlS8uzbFEmtKXld9MtOplVl4N/mPpQZHpbCcF7/6vF+5CKk4lOv9pktivuzLO13KNurnbcvU1VQWecKHf0aJunDd1+mjIemCap6SO0TVI8+o44SSKSd71H9yJzXVP4zVTlf///EQk="
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "White",
                            "Something that is white is the colour of snow or milk.",
                            "data:image/webp;base64,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"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Yellow",
                            "Something that is yellow is the colour of lemons or bananas.",
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
                            "The entire structure of an organism.",
                            "https://www.anglomaniacy.pl/img/xv-body.png.pagespeed.ic.VrPozNS9ng.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Ear",
                            "The part of the body that senses sound.",
                            "https://www.anglomaniacy.pl/img/xv-ear.png.pagespeed.ic.CU4NdOtmku.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Eye",
                            "The part of the body that gives us sight.",
                            "https://www.anglomaniacy.pl/img/xv-eye.png.pagespeed.ic.sVV10Iwtur.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Feet",
                            "Have toes and help us walk, run or dance.",
                            "https://www.anglomaniacy.pl/img/xv-feet.png.pagespeed.ic.SmyAGevmwL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hair",
                            "The long strands that grow on the top of our head.",
                            "https://www.anglomaniacy.pl/img/xv-hair.png.pagespeed.ic.j6ABDqdL88.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Hand",
                            "Has four fingers and a thumb.",
                            "https://www.anglomaniacy.pl/img/xv-hand.png.pagespeed.ic.Zyu2wRzUx5.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Head",
                            "Contains the face.",
                            "https://www.anglomaniacy.pl/img/xv-head.png.pagespeed.ic.7xGSG8hkbe.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Legs",
                            "We walk on these.",
                            "https://www.anglomaniacy.pl/img/xv-legs.png.pagespeed.ic.jR0pKfqOXo.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Mouth",
                            "Used to eat and talk.",
                            "https://www.anglomaniacy.pl/img/xv-mouth.png.pagespeed.ic.jTSXoYm7fL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Neck",
                            "Between your head and your shoulders.",
                            "https://www.anglomaniacy.pl/img/xv-neck.png.pagespeed.ic.mtxwbT_Sro.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Nose",
                            "We sense smells with this part of the body.",
                            "https://www.anglomaniacy.pl/img/xv-nose.png.pagespeed.ic.5TFaMnM-uT.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Tooth",
                            "One of the hard, white things in your mouth.",
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
                            "Bath",
                            "You fill it with water and use to wash the body.",
                            "https://www.anglomaniacy.pl/img/xv-bath.png.pagespeed.ic.uB6miierqO.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Brush",
                            "A device used to brush and tidy hair.",
                            "https://www.anglomaniacy.pl/img/xv-brush.png.pagespeed.ic.0X3s6eyE6w.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Comb",
                            "A flat device with narrow pointed teeth.",
                            "https://www.anglomaniacy.pl/img/xv-comb.png.pagespeed.ic.4TNQjOLNjJ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Mirror",
                            "When you look at it you can see yourself reflected in it.",
                            "https://www.anglomaniacy.pl/img/xv-mirror.png.pagespeed.ic.wR4A4ITISX.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shampoo",
                            "A liquid soap used to wash hair.",
                            "https://www.anglomaniacy.pl/img/xv-shampoo.png.pagespeed.ic.waMOpaxyIy.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shower",
                            "Sprays water over you.",
                            "https://www.anglomaniacy.pl/img/xv-shower.png.pagespeed.ic.UiDmUVTwFm.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sink",
                            "A basin for washing your hands and face.",
                            "https://www.anglomaniacy.pl/img/xv-sink.png.pagespeed.ic.dhenp5BkdL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Soap",
                            "Made from the salts of vegetable or animal fats.",
                            "https://www.anglomaniacy.pl/img/xv-soap.png.pagespeed.ic.Zc7hc7vq7v.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sponge",
                            "An absorbent object used for cleaning or washing.",
                            "https://www.anglomaniacy.pl/img/xv-sponge.png.pagespeed.ic.nxZGXvThZd.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Toothbrush",
                            "A brush used to clean the teeth.",
                            "https://www.anglomaniacy.pl/img/xv-toothbrush.png.pagespeed.ic.oCrKJFYsF4.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Toothpaste",
                            "A paste used to clean your teeth.",
                            "https://www.anglomaniacy.pl/img/xv-toothpaste.png.pagespeed.ic.Yrr8cEEVyi.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Towel",
                            "A cloth for drying or wiping.",
                            "https://www.anglomaniacy.pl/img/xv-towel.png.pagespeed.ic.lJWcXp8ukA.webp"
                    )
            );
            return vocabularyDetail;
        }
    }

    protected abstract static class GradeTwoVocabulary {

        protected static EnglishModel englishListGrade2() {
            var vocabularyDetailCloth = getVocabularyDetailsCloths();
            var vocabularyDetailFeeling = getVocabularyDetailsFeelings();
            var vocabularyDetailInsert = getVocabularyDetailsInsect();
            var vocabularyDetailKitchen = getVocabularyDetailsKitchen();
            var vocabularyDetailsLivingRoom = getVocabularyDetailsLivingRoom();

            var listVocabulary = new ArrayList<VocabularyModel>();

            listVocabulary.add(new VocabularyModel("cloth", vocabularyDetailCloth));
            listVocabulary.add(new VocabularyModel("feeling", vocabularyDetailFeeling));
            listVocabulary.add(new VocabularyModel("insert", vocabularyDetailInsert));
            listVocabulary.add(new VocabularyModel("kitchen", vocabularyDetailKitchen));
            listVocabulary.add(new VocabularyModel("living_room", vocabularyDetailsLivingRoom));


            return new EnglishModel(
                    2,
                    listVocabulary
            );
        }

        @NonNull
        private static ArrayList<VocabularyDetails> getVocabularyDetailsCloths() {
            var vocabularyDetail = new ArrayList<VocabularyDetails>();

            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Blouse",
                            "A shirt for women and girls.",
                            "https://www.anglomaniacy.pl/img/xv-blouse.png.pagespeed.ic.PVpPh1srSL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cap",
                            "We wear it to keep our heads warm.",
                            "https://www.anglomaniacy.pl/img/xa-cap.png.pagespeed.ic.jfjxV2Px78.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Coat",
                            "We wear it on top of our clothes to keep us warm.",
                            "https://www.anglomaniacy.pl/img/xv-coat.png.pagespeed.ic.0YJylO7S5s.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Dress",
                            "For women and girls, consists of bodice and skirt in one piece.",
                            "https://www.anglomaniacy.pl/img/xv-dress.png.pagespeed.ic.IHd8g1j_Ap.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Jacket",
                            "A short coat with long sleeves.",
                            "https://www.anglomaniacy.pl/img/xa-jacket.png.pagespeed.ic.mTbNEa9Hou.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Scarf",
                            "You wear it round your neck to keep yourself warm.",
                            "https://www.anglomaniacy.pl/img/xa-scarf.png.pagespeed.ic.zG1z4xgSoL.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shirt",
                            "A garment with sleeves, a collar and a front opening.",
                            "https://www.anglomaniacy.pl/img/xv-shirt.png.pagespeed.ic.RRSKEE-VSA.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Shoes",
                            "You wear them on your feet when you go outside.",
                            "https://www.anglomaniacy.pl/img/a-shoes.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Skirt",
                            "Like a dress, but with no top part.",
                            "https://www.anglomaniacy.pl/img/xv-skirt.png.pagespeed.ic.E12pEkg92N.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Socks",
                            "We wear them on our feet under shoes.",
                            "https://www.anglomaniacy.pl/img/a-socks.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Sweater",
                            "Something to keep you warm on a cold day.",
                            "https://www.anglomaniacy.pl/img/xv-sweater.png.pagespeed.ic.lnjVXMhJeN.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Trouser",
                            "They cover both legs separately.",
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
                            "Furious and mad.",
                            "https://www.anglomaniacy.pl/img/xv-angry.png.pagespeed.ic.IoefhbyxXY.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bored",
                            "Tired and uninterested.",
                            "https://www.anglomaniacy.pl/img/xv-bored.png.pagespeed.ic.TB0CGcKCu8.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cold",
                            "Feeling no warmth.",
                            "https://www.anglomaniacy.pl/img/xv-cold-f.png.pagespeed.ic.UWKs1Dui8U.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Happy",
                            "Fortunate or lucky.",
                            "https://www.anglomaniacy.pl/img/xv-happy.png.pagespeed.ic.crNMKF58iO.webp"
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
                            "Lives in a colony.",
                            "https://www.anglomaniacy.pl/img/a-ant.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Bee",
                            "Makes honey.",
                            "https://www.anglomaniacy.pl/img/xv-bee.png.pagespeed.ic.YounKdLadq.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Beetle",
                            "Has a biting mouthpart.",
                            "https://www.anglomaniacy.pl/img/xv-beetle.png.pagespeed.ic.CtwoLowprK.webphttps://www.anglomaniacy.pl/img/xv-beetle.png.pagespeed.ic.CtwoLowprK.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Butterfly",
                            "Has beautiful wings.",
                            "https://www.anglomaniacy.pl/img/a-butterfly.png"
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
                            "A kitchen device for cooking.",
                            "https://www.anglomaniacy.pl/img/xv-cooker.png.pagespeed.ic.WxEVYqLbIR.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Cup",
                            "You can drink from it.",
                            "https://www.anglomaniacy.pl/img/xv-cup.png.pagespeed.ic.t0IZcBQbV6.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Fork",
                            "With prongs, used for eating solid food.",
                            "https://www.anglomaniacy.pl/img/v-fork.png"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Fridge",
                            "Keeps food cold.",
                            "data:image/webp;base64,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"
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

//    @NonNull
//    private static ArrayList<VocabularyDetails> getVocabularyDetailsDayOfWeek() {
//        var vocabularyDetail = new ArrayList<VocabularyDetails>();
//        vocabularyDetail.add(
//                new VocabularyDetails(
//                        "",
//                        "",
//                        ""
//                )
//        );
//        return vocabularyDetail;
//    }
    }

    protected abstract static class GradeThreeVocabulary {
        protected static EnglishModel englishListGrade3() {
            var vocabularyDetailCloth = getVocabularyDetailsSchool();
            var vocabularyDetailSummer = getVocabularyDetailsSummer();
            var vocabularyDetailTown = getVocabularyDetailsTown();
            var vocabularyDetailTransport = getVocabularyDetailsTransport();
            var vocabularyDetailWeather = getVocabularyDetailsWeather();


            var listVocabulary = new ArrayList<VocabularyModel>();
            listVocabulary.add(new VocabularyModel("school", vocabularyDetailCloth));
            listVocabulary.add(new VocabularyModel("summer", vocabularyDetailSummer));
            listVocabulary.add(new VocabularyModel("town", vocabularyDetailTown));
            listVocabulary.add(new VocabularyModel("transport", vocabularyDetailTransport));
            listVocabulary.add(new VocabularyModel("weather", vocabularyDetailWeather));

            return new EnglishModel(
                    3,
                    listVocabulary
            );
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

