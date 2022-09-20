package com.maths.beyond_school_280720220930.database.english.grammer;

import android.content.Context;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;

import java.util.ArrayList;
import java.util.List;

public abstract class GrammarList {
    public static List<GrammarType> getGrammarList(Context context) {
        var identifyNoun = getIdentifyingNouns();
        var getIrregularPluralNoun = getIrregularPluralNoun();
        var getCommonAndProperNoun = getCommonAndProperNoun();

        var list = new ArrayList<GrammarType>();
        list.add(new GrammarType(context.getResources().getString(R.string.grammar_1), identifyNoun));
        list.add(new GrammarType(context.getResources().getString(R.string.grammar_2), getIrregularPluralNoun));
        list.add(new GrammarType(context.getResources().getString(R.string.grammar_3), getCommonAndProperNoun));

        return list;
    }

    public static List<GrammarModel> getIdentifyingNouns() {
        var list = new ArrayList<GrammarModel>();
        list.add(new GrammarModel("Ball", "This is a ball.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F93388ad2-3355-495d-94dc-4199d2cfa2c1%2FUntitled.png?table=block&id=802407a2-e208-4bcf-9708-b35131e85677&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=220&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Car", "This is my car.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F34e712a0-0fef-48ea-8725-87603629b8a6%2FUntitled.png?table=block&id=1ee1003f-c458-4b74-904d-1faa01d64b91&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=260&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Tree", "This is a tall tree.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fd683c387-ae01-4472-ad54-bc5a2ec4aa43%2FUntitled.png?table=block&id=bb00269b-f605-44c4-9069-2b1e09d65a20&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=190&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Book", "It is Evan’s book.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Ff99a1a13-d0cd-45aa-9cf5-5a9facbf47d4%2FUntitled.png?table=block&id=b3b749a9-983f-4fb2-ba66-a6b71d8dffbb&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=260&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Woman", "This woman helped me to walk.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fb737438a-6db0-44bd-97d2-416b329420e1%2FUntitled.png?table=block&id=f9261a82-e0ef-4cdd-be84-9b2a77f5d210&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Teacher", "Our teacher is very friendly.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fbc4b79ef-c959-4cc2-a28f-1e38efd7d14b%2FUntitled.png?table=block&id=bf8296d4-9779-43fb-a4fd-2e1536d2b133&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Music", "I love music.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F1fa45aa4-04e7-4ab8-92ae-642964063b84%2FUntitled.png?table=block&id=4c15503b-4878-4003-bc67-543b636dbbd9&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("School", "We go to school every day.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F2a4e4370-d8a0-4915-b435-849a312e6528%2FUntitled.png?table=block&id=a2efbd27-4d13-46f0-a350-2cce2c47a5a8&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=380&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        return list;
    }

    public static List<GrammarModel> getIrregularPluralNoun() {
        var list = new ArrayList<GrammarModel>();
        list.add(new GrammarModel("Child", "Children", "1 Child → 4 Children.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fb6a452ee-14f3-4cb5-848a-9868a1e3e6eb%2FUntitled.png?table=block&id=7ef2ef65-3ace-4f1f-be45-948eccfa7b9f&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=480&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Man", "Men", "1 Man → 4 Men", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fc66c2371-a40c-4daf-907f-f6c550f9be6e%2FUntitled.png?table=block&id=216feb05-ad32-4118-bd1e-bf584555995c&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=380&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Woman", "Women", "1 Woman → 2 Women. ", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fe4f3992a-511a-4d82-bb4b-f83dd31229e2%2FUntitled.png?table=block&id=2473323f-4a04-4ff3-aa6c-12a47c529015&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=240&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Person", "People ", "1 Person → 3 People.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Faf0e6311-183f-48a3-bfed-b1df72626f71%2FUntitled.png?table=block&id=ea21c27f-11ef-4b98-b164-42b1c27ab760&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Mouse", "Mice", "1 Mouse → 2 Mice.", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F7fa23e74-dcb3-44a7-853b-c5f591ae14d0%2FUntitled.png?table=block&id=24596e8c-cc42-4dcd-ae70-8b030ea96a09&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Knife", "Knives. ", "1 Knife → 2 Knives. ", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F743a703b-657d-4f8c-a19d-ce5090dd550b%2FUntitled.png?table=block&id=7231b3f8-8a08-4ed9-8fb1-5f66cbea01e6&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=380&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Wolf", "Wolves", "1 Wolf → 2 Wolves. ", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F70981328-0e50-4a3e-8665-455c5ca6a637%2FUntitled.png?table=block&id=37774d6a-dd8d-47a1-973e-e590e26cce56&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Fish", "Fish", "1 Fish → 2 Fish ", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F98c5387b-80af-448e-abb1-09e436dab864%2FUntitled.png?table=block&id=c74a93b5-4d5d-433a-9b71-9fc9ec500f4a&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        list.add(new GrammarModel("Sheep", "Sheep", "1 Sheep → 2 Sheep", "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F4178e312-155d-4d33-9a12-de71e34e420b%2FUntitled.png?table=block&id=95df0aeb-958f-4b4b-8813-1976dbd9737b&spaceId=e3f3ef07-5851-41bd-9712-14462db96413&width=290&userId=e39db595-1844-42cc-aff8-1816b7db057d&cache=v2"));
        return list;
    }

    public static List<GrammarModel> getCommonAndProperNoun() {
        var list = new ArrayList<GrammarModel>();
        list.add(new GrammarModel("Bus", "It is a common noun.", "https://img.freepik.com/premium-vector/yellow-school-bus-passenger-transport-transportation-children-school-back-school_501907-341.jpg?w=2000"));
        list.add(new GrammarModel("Book", "It is a common noun", "https://www.adazing.com/wp-content/uploads/2019/02/open-book-clipart-07-300x300.png"));
        list.add(new GrammarModel("Students", "It is a common noun.", "https://www.pinclipart.com/picdir/middle/6-64019_-dear-students-owl-theme-classroom-school-clipart.png"));
        list.add(new GrammarModel("River", "It is a common noun.", "https://static.vecteezy.com/system/resources/thumbnails/000/365/612/small_2x/hczv_zd5f_150211.jpg"));
        list.add(new GrammarModel("Doctor Mary", "It is a proper noun.", "https://i.pinimg.com/originals/8d/d2/5d/8dd25d9051cbef9c3a4518b67a7abdf6.jpg"));
        list.add(new GrammarModel("Earth", "It is a proper noun.", "https://thumbs.dreamstime.com/z/earth-clipart-earth-globe-simple-vector-clipart-earth-isolated-clipart-earth-clipart-earth-globe-simple-vector-clipart-earth-225935495.jpg"));
        list.add(new GrammarModel("Sunday", "It is a proper noun.", "data:image/jpeg;base64,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"));
        return list;
    }
}
