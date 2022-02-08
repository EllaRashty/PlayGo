package com.ella.playgrounds;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParksData {
    private List<Park> parksList = new ArrayList<>();
    private Context context;
    private CallBack_UploadParks callBack_uploadParks;

    public void setCallBack_UploadParks(CallBack_UploadParks callBack_uploadParks) {
        this.callBack_uploadParks = callBack_uploadParks;
    }


    public ParksData() {

        // init the all the parks
//        createParks();
    }

    public ParksData(Context context) {
        this.context = context;

        // init the all the parks
        createParks();
    }

    public List<Park> getParksList() {
        return parksList;
    }

    public ParksData setParksList(List<Park> parksList) {
        this.parksList = parksList;
        return this;
    }

    public void getParks() {
        DatabaseReference mDatabas = FirebaseDatabase.getInstance().getReference().child("Parks");
        mDatabas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot key : snapshot.getChildren()) {
                    Park park = key.getValue(Park.class);
                    parksList.add(park);
                }
                addParksToMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addParksToMap() {
        callBack_uploadParks.UploadParks(parksList);
    }


    private void createParks() {
        DatabaseReference mDatabas = FirebaseDatabase.getInstance().getReference().child("Parks");
        Park park1 = new Park().setAddress(context.getString(R.string.park_1_address)).setLat(32.119612).setLng(34.842249).setPid("park_1")
                .setWater("yes").setShade("no").setLights("yes").setBenches("3")
                .setName(context.getString(R.string.park_1_name));

        Park park2 = new Park().setAddress(context.getString(R.string.park_2_address)).setLat(32.114151).setLng(34.817486).setPid("park_2")
                .setWater("yes").setShade("yes").setLights("yes").setBenches("1")
                .setName(context.getString(R.string.park_2_name)).setParkImage1("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHtQss_oVsun7guWw4Y8trlLCEnR4t6DzSHg&usqp=CAU")
                .setParkImage2("");

        Park park3 = new Park().setAddress("26,HaParsa St 16, Tel Aviv-Yafo").setLat(32.120926).setLng(34.837358).setPid("park_3")
                .setWater("yes").setShade("yes").setLights("yes").setBenches("4")
                .setName("Tzahala Park").setParkImage1("https://static.timesofisrael.com/blogs/uploads/2012/07/photo-15-640x400.jpg")
                ;

        Park park4 = new Park().setAddress("Revivim St 15, Tel Aviv-Yafo").setLat(32.117278).setLng(34.832106).setPid("park_4")
                .setWater("yes").setShade("no").setLights("no").setBenches("2")
                .setName("The Pagoda Garden").setParkImage1("https://www.google.com/search?q=slide+parks+playground&tbm=isch&ved=2ahUKEwjJhuXf5_D1AhUMbcAKHf0mAGkQ2-cCegQIABAA&oq=slide+parks+playground&gs_lcp=CgNpbWcQA1CIDFjWFmCkGGgAcAB4AIAB4gGIAaIHkgEFMC42LjGYAQCgAQGqAQtnd3Mtd2l6LWltZ8ABAQ&sclient=img&ei=YcACYonSBIzagQb9zYDIBg&bih=619&biw=1280&rlz=1C1CHZL_iwIL751BG751#imgrc=qcNlOG3kC7erqM&imgdii=b6UBbkHAvHeSfM")
                .setParkImage3("https://i1.trekearth.com/photos/89575/playground_labyrinth.jpg")
                .setParkImage4("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBQVFBcVFRUYGBcZGiMcGxoaGyAgIRoaHR0jHSMcHB0dIiwjHCAoHR0dJDUkKC0vMjIyIyI4PTgxPCw0Mi8BCwsLDw4PHBERHDMoIigxMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMf/AABEIAMkA+gMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAAEBQIDBgABB//EAEEQAAECBAQEBAMFCAICAAcAAAECEQADITEEEkFRBSJhcRMygZEGobFCUsHR8BQVI2JyguHxM5Ki0hYkQ1RjwuL/xAAZAQADAQEBAAAAAAAAAAAAAAAAAQIDBAX/xAAoEQACAgECBgICAwEAAAAAAAAAAQIRAxIhExUxUWGRBEEFcSIysRT/2gAMAwEAAhEDEQA/AMlggPEmpKRmBcJqQoOxAG7Zaa+kdg1kZQAczvlOqWzAf1Fm/ui3EJyYp5ZAE1HmZxlJr/dS/WCcfw4oSZiAElCjlSAaiWrfukF73u0aNWtvolPc9w1J61ZW5Uv0d2DAWAFx03hlxskSpgIHkU9jzEAmo7979o8+HkibOnJTULRLKSfulK61LMCoQTxQJVhwpQBWULWoNXmAASSalgrfR4F/Vg/7DsTkSpWeZlZJUs1uGI/9ddjrA3w5w7JLl+KgqWr+IrMXOdZKmSPsqFtDToYrx05MyTKkpSrOE5yQHBTLTm5hY5lFCf7idIc4Fbo8RWbMouHyueUKGjB6HTtGqlbM2qQDxXDqlSlrCHWgOkEgA5jlZkmo5r/nF2CXXxDLJWoUC8wKAzHNnAIsCW6t1jx2aFlMgpUQQVTCVMAh0muQ5iMxdgbJW9A4iiYwRzDMEhXMpyogB8xPlLaHQ9Id7irYLVhT5llwmuQDlfYkebbS4pHuIw/8PkuKofdNQzsBUAMSzPsYt/aZVs/mDqLsxBrr+vlFScYMwC2CGJJY0UPTX0q0WpIhpivjS/EkrSzlINK0pdJLFq+hDaQMmZ/FQLBhofuq+oI/V2GIlKBUSCSkOHaqGNwftEmmxURqWWSE0kzFOwSQaeYpISWp1Lf03EJy3spR2oM+HFpVLUpVSFXoOYpGUDdqnXXQVipfiTFF3IzPTlcMSANRmX3qfWXAaSUFI/iLeoLFIBqrm/lZIPyIi3hskzDNCiAXTkAcO6QsJHWkty9C93YwpbFadx9gFkJJCWcUcvRmuKOSLCwaC+IKIlrQHcSyKOS5HKzVzFTM2sL0zkpCSFE5UggaVFhqw9XJOsSmTQJUwrUQyTMCrcyWIfQMoAZe8JsEtxphZyVoSgsOQEHowYh+hgdM8OsEMc1FAUSWcK/pKgeldXpVOkUQATmSwSfvBgy0l71BYULsRslOPmS5iTNargMOUklgFv5SCaPQ8yaFnQ6HcvF5BWiZlAPur5QU1v5gxat61MD8KnBCikl0omKL/wAi0qXUdMuXaAVmYZiEhCiM+dIocp8NboOzpCkg0AyoF6EHh08pmKUUkpUmYHFQsy5aAGf7yTmcmzPagM2c+YtQKkNmaodw+gHZrwtxUwZAWIcc1PslnILNsS+2lXcYZSShrEVrrv2rAk3Cg5kczgEpBIqDua0FU+ghWhCbEBSjQByUZtHOYkkaOwKb6taoLnzg4UCyrEPeos+5LAjU94BxCVy5stExikqAzEjmANtgsKIUHvXUGGmJQlQKUsFZWZVqvQtVNSNm0aHYUDYDDJGIxC2JGSXXdJzHKK75jtVMZ7iM+YmZOQnm8NIBUCCypqxlO/8AxkjrmVokw54TjJnjz0LFUCWKkVoSLXZ9hoekL+Joz50JLLnT0JfUJQhCifRLl7QndFqrEPFiPDIQ4GTNXUqLG4ehQw6e0bLhq0ScHLlhChllJSSGqcgGY130jGfEcpMvMkBmlpYbUJ3Pt1A0jUSULWkPRCWyga0F3vqNiPQxUYo48cm8kmXz+ILmKKQKEtSoGWwV0SdNS5tb2WilC6jcs597D/cWScNux2oKD0Fb6/OCUSWLDXWNoqjVuyhEvUu/X9XieXpByJHSJfs52i9SI0s+KKbwpM6rqJSo65hYbJLA66b1jbIkgol0HiCpztQqKiczOzKOlS3SMKJZEuYsAqlBQC5SyHS4ooMWzBRUnMEuxsXps+HYt5QW2cEDMQfMCzVc1FQ5NagtSOKDo6ZITcISqRjpsuiUKRmS9WAWFs+rZlV6ekGYmYBL8R3yyk+axIBd/UACtQNHqFxI5ZiJhBKnKWBcqzpyl30cE/WJ8ZlLITKFFEy0HKqgDVfdysKPUUtCvZodb2NuCS0+CmY+RcwoQHJORBVlCBzPRyp+4tGk4PKDq/iAolrKUszBlNsSUjKB6GMThsUQVyk5lJCiEgkpSnPzAgPVkiYbHToRpMFh1TCmQlTpDmcQFZSMxVkzBnUo5uXRBXaj1e1IkvWRMXNnkKU7CUlaSyUCiMxBykKU8wOHGZADERKWgqyIWcmQLSrK4yqSvKGfypLKIHUaVjR4jDJMsoUyRMBSdDbzUZiA7MzRluDcSlozTCVEukMp8ylqCc3mbYKcsGIO0CF1LcfhzJWP4iSknmKiABmpShzA2bXR9KMPLPiLzsTlCglZ5SS7sQ4J8tBuK7NTLUQVulSlAFizJ5QMgUercxS5FLQkmrVLUlgAErUkGpSAopOQsl0uCojsNhmaGNZhJQpyVZA6Q1VILhQp5qsDqDlOsJeLlKMEVIV/xzlzenhqmFRa9Mk3s4GzwXgJhmIUzVJVd8uYMKPYp9w/cATpv/yuKQADyBSk/dQUJNrs6SK9LWBJCTLOFzkolyxMDZk+GrKKg0FAxV5CCALqLtWCV8QCSqZLl+Y58tBlKkkMk1SCEy0OxLkqubAYnBzfDQoEMMiRR8xRzEUq5KSmlXeu9ElXiqlS0ls5IAqGOUErPVKc7H7xbSrSQbmm+HyWCixUACALBxcBiSa5blmvFnGpq0y1TUqStUvnKVUzJRXKWtYVrta1eGmy5YmIMwZkFsyWZstHNNA3yEV4/iAMoqyZ3YkkNmZQ5AFBiNC4a3aKdErVqD5swS/CSpfIeUubFKSxe/o+2kLcTg0KzpUpUwLDkENlStSiebZtXOtDGf4hMKpKUlXMhpagotRLAKIu+ZFYIWZxmZ84zo5coNMuVKgAUqGY8xOmu0GpD0sOlLVKSwHONjrLm5moCwUlKyRUChHnAFOGKTPyIHKJ2YOA5BlpSpCxQpbmAFmYxLF4da3lgrT4qVABwQlRAapqQSiUmjM9fMIGwmKlqmSpgSU58pcliCucp0kfaygu5cdXhNoaTN5g8S6SghIKbMfQFvaKJqz5iGYEKIplBZ1NoxD3sDAyMQU5UMXUWJuGq6wxP2m9zBy0ggrFDrqC9w1x9KRNoKYo4qStSEl8xUnL/VzB3ZtX/TEnhUwrfM2ayqapez3DC+tDrAePlkiXUeGpYSFJDlDBRCSCQWe3XKNA88IlKUpnIoAcs1tnvarONnSQfsiDVTsrTaonJwv8WdMQzoUkXGiappYG9dW0EJsLNRNxcypyynYoDuV3FAWIqkvYEu1I0eDmZfFLOVzlsP5UpQP+rs/eFnwxKeXMWnK8yapZozAHKK0Y0YUdm3hW20LZJma+JZSTMmpSCLPqSVJDOf1paNrKwFSCjL1BFuooAQKRiuP4jxMSoB+aYgO/mBCUimgIF9Q0fQVhIFRawAHyH5xcU2ceKVTl+yCUAnkCr+c1FK031D/W0EoAQdH1q/qfygQz1GpoBev00ipOJzUFtevS9a0e2kXp8nRqGwmBtoj4ogFU73Py/TxR4nSL0onUfF0zPCRiZUwMSlg4VyzEqBTS4BSSX7ejrhqV4eXLWU5pc0AnMQyF5spvbRzWtI742nSsRMRNQ4WORRTZYukhyCGdQtqNngVHEiJaZawlSBTOQWFCkZgDyggJFyk1etI4+jOkN4ySpJWlIYFKujheUgV8opp2jl4nMZKiGDKUocrruQU6HzIABblBgbISlcpxlVLOQqNGHNrYs34sYlLmSycPLUQkeFzEO6Aogmoq7DIGYipoQBE3uV9B/wCy+LOWEKBCgC7h0kUCEBRCgQqhIIZmoTy6XAYkS5fhpR/xF1BOYchIqHq/MRYHQxkfAQJ6TKWPDIaWkfaemUAswzAM7CuppFuIwyj/ABCHCGzy3HIC4DtVVA7l9LGNEZs2qOLmbLo5JNJhzMOYhggsS3VgxoTYh8Ew6FS/EIK5pIdfM4TllF7UAcUoBTpA2EUkIylTAVoZgGVQccgVRnag0JpRvPhDiEpInomzFD+MSFZ1BKUgZaZjUU2Oo0o6EaXw1osoANsxq18wr2cVdmhZi5AWpSZgyZkBnSUsUnzXJDOlT2oa0Iiz954NApiUZRYFYUakmz17mv1hTxjjmGVLKUrQZlWYK+2ClTEAigUVC3ME7QbgiCcV4Zl5Q4IAHKz6FIFKgh7Frh2UIFxaz4c0g0XJykKYlWRRSCW1KZiTSlohieOylJKUElIqBlPLcPVTsHCh90/NNieIJV4oRWilGgoWDh09UghO5VvCm6RUVbNjiJpWMNJl8yggqLk8oCUpcsQSolSmcita6pcbhTIxUvMkZJqme1iMwAZ6Agm7kqNTEODcZlomZFKyryhL3zHMdwwdkgP2rF3xBOlhUsAOlM1C1ByOWy8tXSogilqQXasKp0PcPiZRmOEhfKCAGdgVZlGgtyuK06tAXF8ehSV+GHQwKgHGVQIYggZSouzBqEU1hFiMWVAEKJNwoODdTmh+05JA6auStVPIQQTQdacpcjb7PrpESnWxcY7jfGTB4alpdleJmFXCKA1B5iy31oEwywEzMLpCkm4IHMSVkmxygnK3QDQvjzjDVidavoWH4CLjxGYUGXYEuGDXLu/9XtXeEpj0Gsx/EEKyMsEhBQA1nIWggVsZaR3fSMxiZqiJSdntcK5Sf/FKQOie8BoxBylG7vo9c3s7xKVU5S4YK5tmSemxf2hOTY0kjZcJ4qkZM6iSSnL0YEVe19PrDBfH8qwxbUpflIarHQuBXtarZMDNMSyQkAEAWc5mBJ+W9K3aKwtTl/s5ipncsRT3fqwMWmyaRrl8UlzDLlqS5KiSWIdhm0uCAoN1g+WAghTlSCnKQzlQ/me6mdnckDKeZjGJw00mekIq9iTUMzkObsxqdTG0XiwZSklGU5VZb0LuFWqEgggh21uIpOyZbA8nHCThFOorUtKjmqSU1AbXfrzPeB5GOAlokZCciAZhAqQwcE25iS9y3uEGKWVoXLA5iXbMAAhKyVAuQWMwnSygH1i/xlIlFRdKyCkvXMFHKaBrmtLU7RMW30HKKXUHxuJMzEpXloZyGDiozpSz+kb1ExaqJCQNytIc2a70HrX3+f8AD5b4uWP/AM4JJcVSp/Tyx9LUnPzMGA6l9PWNVdHB8etUm+4rmoWumboyXoam9iHAHrFklyAwDF6k0YHLppSK1lYUGPKFEULEcpdJqw1YhtqPFZSQ6XpVW7M6W7UBh2zqpdhijKQR0B7P/wD1T/MGeEjYQtQolQSkPTpU3LGwFB+VoPThpjCg+UCl5Bx8HwYTypJcmvv+niCZjvLUpyKh3se/4e+sV4ddL/KKsShly16KOU+tHP1jA1GCZ6gEhRdCXDFyADdk0IQfujaja0TMypilMzl2d2YD3HX9GCZinFibl9v1X0i7iKVJVVgGZNBQPR2u1i+0L7A9yVBUS4bXUM3szAaWi+ZjlupSpjPdjlFNwGFHMBmbmZdAW06H2/KA8WolC+2+weLU0uiJ031GxnvdRI1q7xUvFIR5mANj7wEk5h0HzpBKZf8ADBNXLNryir+ihD4jDSWniCRqD6f6rAp4oCHAAGjkP6jT5vFOHwqOZ0AgKLHo9PRmiXDcMgoQSgEqSTa+U1f9aQtbYaUiEviCsz+J2QoctaZacpHsbWg7xMpURaZLzJpQOQCkbh8yR0Kb61T5EsZWSPOHpYFw3+YNWEmUoJQ+ViDSgUQ+tHyp2tEyXkqLFk3GIzKIXlUdT7WZj6v3icjjM1KQhaitAUCmjjsKOkaMHHSD5a0UaVcmjJuHo99IE4gqYqXmCECXSzVq7VD19IWy+wHGCx0tYzAkEJCShVHYVyks7l9+sQUvmYMUgOdtCa6WHu0KJC1oopIOgqKFnu0X4WbmVZwAXG7AqavUC/XeJZaZcgFwKdfZ/aCQjlBJYKSWPUEfpoWKn7a6xWucpgCXrTvCSCw5K2Kq00+X5xJM/JlewCm6GxqWysWMBINaF7H1sflFvELAZqO77v8Ag4tDrcVmilcZyucgbMS5+0NH/l6N1ij94IK8zsTdxQqBcEtUZm0BhUmajwwVEsGTT7ug6WG+lIoXinPKkDdRcsb60sNoq2LYe8NWRMzulYcWJcsKiz6ku28bhOJVOSJhCUlILWLZQxFfK+Zuo7R8sTPZJLlRdy9XHc77Qzw3HZqlypefKM6RSnmUxtvmMNSqwkug6XxADxpgSylKEoAmuUAqUf7lLA/tDRDg89SwlHmJU73CUudP6jQbt3CjjMzJiJqQEpGaoTZ8oH67mKMBxCYk5gvKAR0s6gAdK7fhBF7JCl1Y9w83LiEmgaYo7sQFEvpd42HDeO+JMyNypY6O24ZtxHzrET8gSo/zP3UlQNupMU4b4iXKWSUJI5biwD7M7v8AWNbWxxfF3jJ+WfXcdPCUJUGyksQTbM4BpYgmvTs0ZxWLXMUQRy5za+UZSoH1NuvaM98Q8SmTFBKpsoJ0SkmncgG4gLhHFEuSpYDPlIKvMwFmqOYP0EJyOtI3kjGEKfM2VJzMbAtamw+vSO/+LRogtpf8ow/EeI5Q6JsspHlKVpL0GhNQKh9wdoB/eiPu/L/MTqQ2jOJnkV9h332i1ZzylAlyC4Pf/Ih6jhEsv5b1qD71icvg6A4cAG9g/r7RhxYjFHCMGqYylqyhYUEgAqUpkkEsNAdTrDD4iwhlplzEnlUkGqWIUUhwRfr1rDvh/BkiXlM2UGSpLLOUEE5hzgFti8VcYksMPhvEQ5UhHiJLoFwCCakDMznQRSknugMSicRY1f6v+Ijl5lAgm4N+0bFfBAgscdhydkrST7EiOxPw9iihQCJkxJFGRKYhuswH2h34EmY6TNICSNQN9RrDJGfwwUgkJJUpQcgKUpgk6CjFrnmPYvh3Cpnho8XD4kiheXLS4CW3LmoZi1Gh0MBglIEvxJ8oKVmV4iSgoyhwa0KTmqz01EMZk8DMNQLlevozdItw08IyplhL8zEVABIsev6tDzE/DsqWk+FikzMxckyZhZI2KEqG1afSMphpiEFQIzMSMwAoXLEE1116QUBZjJhBSbkEO42INYZ8MnFUubQuJIIJBu6fetIDxWKC0JQgTSywQ+VgAXoEov6mG2ExiSJwSiYHlkpzEEBmeyQ5Jb2iWnQ0xWMSSpIyq3JY6pdhA+JWsymZTMNDoRY+kaLArlnLlkTTcnllnmJuCUlX3qPFuL4aVS1GXhpudnSMqWcF6kAQqfYVi6dwxbArKgXqEyyoJKk2JBv0DxTJwK5S1CYAUKSS6TcNmS1nBIHaNuvCyyyihSWUVALUgFJJBLAkAmjAuzGF3FMqmukAj7aFXU+VkKOpvoBEubX0NXZlE4LMmqiFAEjlJBLUDg0D6tEU4NfKSaAg2Ohf8I1aUJ+7+P4RMIQaD6fnE8WXYmmZQYOwKmLE0BehAb5xerD2SSSBd2B1a/cxo14VJU76NfS9rPHsvh4NM6h2Yfo0gWVhTEUjh8tmKnci6kgVoGP0EeTcPLDpGfKBSgOalST+voz6fh6AAqIs/LX6a9PwApXw1JBGVTHV0j5gvBxApiKVg0kWXVgasG6ddfSGHCeGS/GlKyKOVaSObV6a7t3gscOYsAsD+oO2vpaC+G4NpksnxKKo5BFNwA8LXf2FO0KuK4AKnzSUKPNfMK6Pd7ARRh+GqUQEhQBURXKwCXN9mS7w4nS0rWpTLcqP2g16NQxZhsMhDumYBlUL0SCCHYDQfjAptPqJoFxXCwsJDqLgsMhS7hmBsL0e8Kzw+XqicO1P/wBY0ktaPEcqclFRLck1TWooBloKlzEVIBJJEwOdevp+njTLNp0mcvxVeO13f+mbXw6W7iXNFau1vVD7axZLlSgG8OZQqq4NCwI8nSNEJNyxpqVN9Lax4jDAABKLl6PcmpoRrWM9cmdVMQKkyWQPCKhlLCmqiasm7ufWJ5JX/wBufdUPzKWHFHJ1enzYx7l6y/8Aufzg1TBxkZoLO9u30i2Viik5iEKOoVbvRj6PFiJexc9hSPBhXck9qxmmhhE7F4dSVPLmIXWktSVpL08swgjsC0A49IGDlM5CFZXIALJNiASA1rmJzeGoNSzsz7Agim1HHrBeJwgRgVJS5CVPW4qI2hJPYaEmH4j4SQlMxKVJI5SEqZNrKBHkJ9Iao+JvEBClIW1mVMQT/wBVhJ/6wMvhElRz5at9kkaM3tHqODYe/hk7VJ+kDyRFQww3xNiEEDKZiFNRXNlB/mfPruWEUnjcxSVzJQQSAE5MwLOaBicwPIKE7nrHiMIhBIQkg+oD3sS0erlSyQ2TMC5ysTygtUPY76PvDjksewsT8QLlLbwUEoBQlwQSHDFxqAAx2PrBuH+J5sxnTlAAd5YW/V1hSiTTX2i+cpzcA9TX0YkxAra4UaXCT2Zztu0J5GxbBi8VMNQJQHSUj8U0HWKJk5RBBmBiGKdGN6J9dIo8RYFlN7e4pvtvHqM5FJbn89umsZ6n3HaO8eaWeYvR3Wr8OsVrQoh8xUd3J+o/KLCiYSwSAfbowHr9Y8Am2Jb6drQrFZX4a3ou3qe148Es/efp3pRns8W/sqiWdz2f5vFisK2WoLli7ita+7QagT3Kf2ZZsstvvF6OHqo6v17wWjCVq366xcmS3mPvGbmySiXhCNSR+vS8XiWpgHLd67xamWLAuOnf6xaEaljXWj9iT8midTZQIEVuf11EXJUNVU7foQUiUHLJubBwxudSf9xJUk9ujW/XWFY1YtnghQZThtXAA19S4p0MG8OQozEuKAElq2BbXdqR4uXlVb7JBF9fSo26wZIlZOYEB9W0P0NrfKLUi0/AjRNUkMpJob50gEbO120NbwGriK1pP8JaUF3OYkZd+YVzdPnDteFSxKUpCndzrX3d4rx4HhqomraVob+rN6xcMi1URkmlF/oV4fEZJhoapZ2dq3NRT1g5GKCmeYOjcoc6FnJ994jwdDrmdAmv/aDugc0YP/pzGubJpm0c3wJJYFfn/SBXMFUrTXQqJfs9oklMxRohKj0yfkTHCUCQSlL6sNDBMqSlNQD0vc9y3yjLixO5SR6mUtqyyPcf4iTzPuD3MchSagoOrFx7R74g3V/2/wAw+JHuGpGZC1F+f0/R/wARYhBUGd/n/iJhCCWdSQGpl176fr0mcI1QSXpQCvo94ys59yC8MpNx7U9miM+WmZLMtYbrV/bVoYIxMzyizah6dv8AcVeGSwLE9QkH3akPXXQp+BYmQQAAXPa2l6xISS3NcensUs/zhrKwhUrKlIJZtIpRh1qUQ3lpah7aE/lC1MjcXowYa+bdyCfR4krDpCSEhQJASw5XGYPYgAWLGG0nDA3WBpQPSvyf6xPEpShBIUktqS5zaCty+n1gjKWpFKLF6JCQPa/X6PFczDpqwL6VP5/jDHDLTMS8s08tlAhqZSCHB6Fj71JRg1FspBfoT2cAE6xOqVi0sRy8Kom79C/69YITh9HYG/WmlLQ2l4AvZu9r+59IIRw9T2QPY+jCvr9IP5MpQkJjg6UcjuAG+W0WSsKmrH3uNdP1+L9PDnoQGZqODXUe513pFKODpTYkZi5c+9zTeHpkVw2KhhfUbNT0PSBsbhikyglLkzEp7Bwoki5oCO5HWNSjAA+VyDqC34Xf5Qt4hgXmypYLkFKydJZE5BJVuVo8RI/utFRg7LWMETJZXktqwam1ax6V9GYsbxo08Pl1ZzsM1fWvq8e+DLSbAV+1en4fowtDFwpGeEkmqQSx6l27fWL0YNQqAp7/AKFX/V4fJCdB8ujepi3Oagg10ylP0qIax+Sli8iBOFmVIRlJ1ZiSOg/xFv7HMN2rvyt8ocTEO3MG3Ba1dQfr9IrTJJurl11+d39YfDRXDXcTjhZMxnZkP25h3aw9osncOmEZcwHtTtsaQX+zqM5ipz4VNfta0Bo/0glOELgvYu4P539Ibgg0RE/7DMs6CBfmZmG9YD43g1JlgqYOpNRXV/qI0czCrainYfq7jp7xnfilBTLY5gUPMYAMpuViQPvKCvTZ4eOC1r9mWfGlilXZgfA8MVqmEJcApGlCx1PcQxmSFE0Nhsfq9aatAPwPMUuXNVUPN06IToQXvGoC1+Z1FiajT3A+W9IeaClNsy+Jh04Yp9hQmS42HVvlvEVSmL56tQf6DwxM5L6v10Ipr09LbRFa5dnD+gJDO4ratIy4Xk6eGLiVGt66H5M0K5+PAUobEjy7GNOMNLd2I1ANvxij9jl7j/uII40upLxv6M+qWqzg3Jqw9HYi0Wy5CjWgHQAbVoGDw1GFQkEa/wBNntUkU0rtppbJTmKskp2DZgXAOxZ6vWkPQ73J0b7io4dYZVSwtcn0boYLRLUQ6UP2SzdKiofbYQykSVkgMBS/Q9Gs4u2useMRQAu33ioGovtbr+EPQkPhiyXg8wJGnqW67D/MESuFLooE2qGJF9AR/qD5mIRLDFTUpQkm3ZrXp6wOvGgslyxe3MzW7lwdNN6w1BFLGVowjE8rhg7IsNg+1YXcUlP4RlqeYJgyILMpwUqSrKHSMpJzB2CQa2hmjFOMyEuTdy77AAl30Z963YbiCFqlicmYhC5IWs0KgUtzJKXBYgO7g0Gl6iki9FHvDcAlJmJmDJMKhMWgLIACwycqmPiBkM9CGLgRepHhqKQRlCTVStRUjuaVf1iOCnFZVMUnItkpUlszZUk8pBSKBdSQ1qC0FheZ6A3JAL1tRncMEh2u+4hyoqJVIWlTcydgAElL68z02/CDQlbHV6EjbWtx2iISuhYAa1DhrUsBRvX3vkLykkgMQCBQuTW4NbULxIytCFWZQ9TrVm7ddOseHDzC5SQxbUguN39NN4LKi3mJ2uPSlG13qY6TNCUuXSE3JDMwqpzpRnP+2FgklCkiqk+o12oSDR6d7ajzsGUomqYlSuZkVNBQAUeta7ncwLP4iqbMBAGVFgxAPU9emlItncTmLPhyiAtTh2cJtmVo4SC56sHdQbeOKlcjB5W3URnLS4zFOUlIdi7G7PXWLkyi1dN2qe419NYhhpWRKJY5kpSEOaqIAYOSn9V6Ra51sN2LfqlY59RuVlYJKWIIGoOuttosRLL2NbX+oD9W+l45My3Tcm3ve/tFiFtpbUsHLs/brBYM5MvqdKvf02fvA6ikE5iKXYn5f4EFrmpoQ6noKdDrFa1OSUsnqzsNyHH+aQ2yUKULSvEmaHKDL8FKvvKcrULsGAZ2qXDgpqetR1Bboe23aPneK4/jcOVYafKlumoKQRmAU+eWQWVbblNxGz4JxjD4qWZkugHmCyOSljal2LV94pphaCcXjUpSVqBBAsRckWBP6vAHEcHmkLJOUryqOdVmLhD2Ar7knUxTikCYdfDB5Ab9z6WBt6wl4pikykklRIT5UkkpK9CQDpfqzbtvjxpK31OfLktNfQx+AUGXhpoWCP4y79EpF9nDWjSiYDYMNQaV7/40aMp8ITFLw4WVAgrWSCpjdnJ9IjxXjEx/DkFRWRUgFQSnv9TRutxhO3JmuJJQX6NPOwyJjApHtXu8AK4ZLJoOoJencVo7WhfwxCEoT4ihNmm5KlP/AGgswv8AnWAOJ8clS3DqTMewmKBLG4BNB3FYmr6Gm6G68KpDtMUkigcs+pDsz0ts0UZp33l+xhOnHY2YVIM0ykGgzpSpZ3DJykdtPWBP3bM+/K/8v/WHp8j1Psa5eICQ6Jaw7MMp5qfao76t9YNw01ICczBqskUA6tR9Nq9oq8UAcwzdAl8ur11717R02eBUhQdqFJIGwJdhpeEr7kKJ03GhflKU7qUl2JFAAHBcjzPsYsSCrM6k1LABJFtMr37aPSJYdCZj8wL/AHD0ZqWNDrraL8LIKQoJLA2ALlmt5UgnVutyIdj2FE/hK1nl5KlnrcNR3q1LVeGWF4elAzL5lNUgk6VIBNDao/GCinLQp0ANCNum0TmqFHqQHc3cszDq1n21goGytMguaDKahgR8y9zanSKMRwiWuXMlqdloKNahTuBq96tF5BdT0cNYaXc9t/R2jpc22UBTMx0v+TbWgsBInGJRiJiJksy0FCFOZZyuxBqARZtb6w8SrMHzOm6SGuNQQKDStqesMVIExBQsApswcHowTWo9CIyyvh+fKJ/ZJxIvlUWOzEpN+lBekVSkTbRrZiWNX1fQFve/yiialJBKqnQF7DQt3Nq32jIn4gxUimIRNQBQKKAU3aikpg3DfEUtdp6QNuUfQCsUsLZLyJGkViES0uSCGzUqTTQW6VjA8e4zMn0H8OUC4RqSC4Us7uHYUHW8O8Xx3DS0KUZiSWsC5P4mMxgOGYnFF5aPDlGvizRlTl3Sm6x1t1johDHD+UmYznOeyHasUFy5QQ5Uv7KfMohiyXIAtUlgBUkCNFw/Cpl3I8RQ5iByhIslJLUD7VObdohwbg8vCoIlgrmHzKWzlN2ALBKX+yKVrV4JJo7AAi6SXtpa1G0rcNHNly6tl0N8WPTu+oTnIcgdvwOjvT/cQmYooNU0BvqGZyS2gqbGtHgaViRVALKrQm50DbEGxZq+kkrzumockO5DnsKFrV/OOc2oKRMf+7rZ/wDEerUEuXLA6aC9wKEQOrMCQEOG0fTXc0b/AFEv2nL5nChtZjtQl3B9+sMC6bOFwQ/Xa19KbmLZaySDQBr3BA2Yn9A9oELnMQA51oL2LHdhT36RQklzkL6tcGxvYwCo84lwqXPQEzUZwPKC4UD/ACqBdB6g94W//CclBeUpUqYbHzJV0mJsR1BB1cxoEqI5XZql7mz6NsfeFXEOJolBlELJPlSQ6RvW+ot9YtNp7EtJ9RfMwGMfJllAEsZgmZgBvlyhTtVvnCX42wqZOFly3dRmhSifMohKg5GgqwGnW8MVcfmzHlyUBJNXDHRmH3R3PpB+E4OmXzTR4qizCrEnTV93NPw04krTkZzxJxaX2YjBYuYMOiUgqyuXSi5JUTXoNt40GAwOKyfwpaZSauua5USKOUtToLV7xsZcsIFU1qTlG9qXFPxgLFEzAXLc3KmgzAFz6Coft2iZTv6HCDikrMtN4NPmHNPmlaWplRlJHdqCw17R2HwMmWDlBT9ok11DgBQDpqzCntGmkTcvLMJTo6rKNaZhyks5bZvSqaEKzPRNlCt/7CCAAAS8Q5NmqSM+MNKzZwQA4JChqa6mlhtpF/hD7sz9esHTZCQU8hU9KV02NdX9QIp/Ypf3U+6ICgtGFWWSt3TQVcqLgUApcPd7dYZYeX4iXUigPyvTrqf8RAs1aXBL3DVNTZ30OtqxfLJYJFS42Grf0sdrQ/shkE8vMQADpTl02rXUFrel8snyvUXsVOxAJBs40+ep8RvYN2o5rR6e8VftBAsSKb83rd6XAe8MRZmJUA4fYs7PS9Qbx64VZ+XZyHOgF9qCzRRnCiQ6QTXlJsKAAi4ZqF7nePJaMpch+xYnuTo50H1qhhBUomgJSGerbNodSXJdvrwUXykkgObhhR3dw436NSITMSoBqu4GapLs5drPQe9dYqE9Cs6UrAGjXJ9RQO1L6uNWIuKyGCQkagNca8oB9dqd48UpRDh1X+yCL2c81rvrcaRVMnU5QksGKgALOG5Rqd+taGJSEKNz5QWAo1+taHrc7GCwCELcOQAKu9G78ta9/pC6fwnDzCAqVLUTqUpoG1cMFUN+veCEzWBqX63PpqGeztZmis5Qp10F6uHJF7OKAavU+pYUSw/DsPLrLkypanYEJDWe6Q4Dg0L0aogtExCg4Jckasz1DAVG7sIVTuP4ZLp8Q0LVSsjvmDBV31ubViQ+IMOW/iKSCknmdiXTdnTd2qWA1gabDYZIVQlwQ9CH5g9S9nL2ahc2ipYAFUvVrEa3Lv7117xPB4lMxylaVaMDQVvlFH6/NjSS5ZCWJZTvR2o4dt+rinyljQDNkpbMCpDudSQ5oLbBvwiiXNUkKzc0tnpVmTdmGurtaDpqjmKXcFLgm4dy9HVqHPTs9C0N5aE0pzORsSxsLkXaJKR5hZoy0FB1qA2r/wAxFUsR84mJqdbn1N7Oa9GH0iCVAJWFpbMAy0hiDTypNXOoI9dICx+JShJyqQky2OUlnFACxLuye73gGHqxOSoS5FWS3MblwLEsb77vFs3FpQhS1kSxqTYKsHOpo35xisT8WKBSmSEqWBzgpLUFQklsxOppS948kYjEYvzFACTRLhILlwQkg5ravd4tQfVkNroH4vjE6eoy5TpAJo5BUHL3bKHb/DwRwvhEt3mkLIAYJcJ0qRqNPUwaMGiUgCSxWogksAaF3JblSHNmDttX2XicpZmAFUCpfrksCSK0vCcq2Q1GwkIQkOAlOVwQEgECtrDTtcQPieKSpReYpiapSkVyk3VW7jXSkZ/j/HRLeWxXNIeiiyAPsqIAL5mo79neMrPxa1rK1nxCq5J0ewBNACLMaaRUcbe4pSrY+m4bjEvEZQhTgXSQxUzkDTvQ9KQ1mAEOAFC4BGtmF/l84x/wxwbIUzptGDpSaZWqFKY0NbaUeNSggsGcEOyiCBTdtvxiZUnsC6bk5aAQSaEGmtRqkAd/1WOQTMzJUCC4ILCoAFyb/SsVuLuGNGUajWxZ3YD29bpUxL1qdzv2ajbbEGAZA4SXfykXKXBten4PHeCkUZVP5j/7Rd4gCSzlyxrW5r2O3o0RzD+b/omARSnNmqHLNqLUcW21PpEJTB86X0sGfRgCSBQm4o7xf4+pA8pBLnMeV3pdnvSBVjMqoCDTUqU9nZNCT0Bbd6QIA5CZaVZmJUQCDVm8tG3zG1O0eYmWJZsANeY0DXA3oxB27xThpwSAEhwkAvn5lWYq6a/lSLVrFObKmlRcs9auTZwBWoveLJKkIFySP5b8pplLBjUEEu4HdxXLAAUVlQAFywIs/lDqJYDWwY3iExCZgJAYAsEgDMHArzWalOzVDxXMSwyXGV7PZnsXVsf8l0MFxinsGUU6EE3saFKi3pWt6eSp5dKeV3y7g1auagFr294MnyTMGY1erh6p1BJqUl01qWagAilaDzFUvKSryk/ZFMwIGvNQNTWFQ0y8LAobkOovoCAQwtR6UpvePcTxFIHiKOVJIYqJ5Q4D9y1q0D2LQl4h8RS6JljOoUqCQGUaEjZ2br6QjlSp0xlqUMrkqISWb+UOM1mcav3ilHuJvsH43jaio+EVFQUanypvokuolQoRTSjQThuFzJwK5y1oBPlJukMUt7sw6ihsw4bhJQIKEMoB8swVNA55mFxtbSD0BTNSoPKQ5D0ZjUO+lSDraC0ugU31FC+FywFIyIZiAp3LipL5uxytRw4rAyOAJmKKkkpZ2IsLEuFCpVo7XaHZkBQrlZLi2ZlN91q0uzUDdjEKIZJUaKBBeoYN10YvTWzxGplbGVTw6ZIJUkDK7BWVWZRdnUB5Q7AEd9SYJw3xSAvIsAhWv2rkEEM1FAgKb8joFqTM5EupyHDKyuBRRBtykFugtGZ47wF1GZLIBArlFVA1IolgSTpq8XqT6k12KpvxSpJVklul2AKi5UTQPbZhUjvSBF8RxsxeZKTKFSfssKVOYuWAsNNrxTwHigQSZqHS4SaB76Zh5n6/SNijEJWkCUQtwCXYsVAlOZYAZn9gO8OSUeiBOzPfujFVVMnZE3LLUwDhtAHsPzqSNM+H1FQJXNmNUOkBNNNT1Zt36aZIEtf8Qh7tWmlEvuAGNXEGTZiAjO48PUqKQyj5QAairbamJ1MdIRcF4RLlpOVGZYoVOSxqDS9BlagNy+hniZZSrMlKHUGASA6kkWcHfqddxDCalSwFSwUJZg7JJD6Jd7F2P3bB3iuXIILk5gb5i7k6J0Fai43iWykC4PFkE+IGV/SbAMA4Pet7x7xBcxctS5UwIWQbAFLkCyjmaoIFNQzM4vXKBAZVQ3mDgB9BmZnNX2J0gXDylBwUHzEAeUukBmKbUt+iEnvY30MFisFNlryzZais6IYu9/KSKMRR9I1/wv8ACZB8Wek2dKHGjMokXtTa9w0NPELA+UZspUSxFAWLbv2jkTJiEBkKpchQZRVrlJBCrvlv84tzbRGih4uUgS8pAY3Z+UCjBVtGLaxRMkkAFTkGmoBazbasA2uhgLDcS5jmBSGYZi4tRq8ztV6sDUs0WrxgUQAQM1Egs3K9aM2vvuYljpk1qCgU/dYVLl+hf09ohJJUrMFB6Xew1IYVDX7RWZZCTkDudPKEu5ID2ZyOx3eJzVrYZagm5a7ChI9DQdHrEjCsyiXeiepf+4tWhO2oi/xFffP/AHX+cBKxaqjKoMXCqEuXdn0077Rd+7ga+Ia18g1gEeYSVMP3E5k9PIWLPuXHK5DAjaOneGlhVIUTmLmj6lhRNa6DRoR4DjWKm5giXJCUh1LVmShDlgSVTMrvQCpO1Ir4vxLFICUqTKCS+VcrmStqEOVEULOkgEU0ju/4ct1tf7OHmGKtW9foe4ZAY2KByhRccwHUJcf56RNM4lROU0FVKtQ3LqB3e1LZoy4+IJyUgmXLIVYkL5st6hYe9REFfEk0/ZlXfym/YqY7Vi18DN49kcxw+fRqETGypdKlMVKqcuV2HlD1LHMdHgDi3FwgpJXkUwAGZmCc1SBsAKF2IGwbN4risyZ9rIaA5HBIFQCSSWBrSFczC5nUVLc0KgT7bekNfj8v3Qn+Sw/V+h/ifiY18NdW80wOMwToC5LA2NOxMLcNg5+KzLWksUk5lEAqdQBCQBuegoK2iuQjwlKCkJUoGomB8qgp7UqDDNXH5hBGWWLDlSQwFgGU1y8V/wAWVdEvYuY4ftv0T4ZwBEogqGdYJZLMQQai9VDLQvlYvpGhKBdKTlDEBd6ZXbqGNS3caZeXxqYH8pcavZ82irPvHI41MBJAQ5v5u33tgA21Ih/AzPt7KX5HAvt+jWrUmZc5SUvQsXtqHepcvaPCoAByp0hi5ck/1Bi+liWeMujj84BuU7ODy9q0pTtHp4/N+7L9lF/dVukLl+bx7HzPB59GilTmygAAh6szA3S9moa9d4mubmcFB30AKmKtaPXTQkWjMJ47Md2QTaoNjo2Zokjj80MwQGsQFPZr5oOXZvHsOZ4PPo0vitmCgczOpKaAAAVY0LdGfrcQQtEwZCcoKHB+0E2CjflbV3qKAimaXxyYQQQhjpzDRr5nbo7axH98zcgQMoYuCBX5lvlBy/N49hzPB59C74lwYDzJYAAUBkBZleUkJbY3ua0a8OB8emSkKlhCDR0rWVCpJAp+g/vEsQVTFhcxRUUswIDBugA1q9yYrEinmUdyWq1tGHo2mwjVfByVToh/ksN2r9EsTPMxWdWZZu5c2FWzBmBoGIHY0h98OKnKUlKhmlJPKq/hlmOXNcEFqFg2weM+iSxfMoh3KXYHSuVjYCoINBDtHH5iQyESkgJysElu7FRD+nd4J/CytUkgj+Rwp7t+jWTFlKcozE2NA75kqFfz6xCUHWeV5h8pSWS5c0evlYW+1uBGWT8Qzx9w01S+rve/WPV/EM1RdSJZO7K7WzNQdIy5fm7L2VzPB59D7iGGWSplMbLQUM4PKQCbf6a8VS1jlSsAG2TKDZ6HSgb0I1hL+/5v3ZY7BQ+io8Vx6aQxym1eZ6EH724H6eFy7N49lc0wefRoFJQoFvMXYMHJ6b9xt2apKMoYmmYtQ6bKdnfW1YSI4/NBJ5CTvmoNR5rHUWjk8emANllsb0NR15q/WDl2bx7DmeDz6NDiZuYBRL3qmlqVfQMaU+daEoUm6yygLWIF0uQ9m7uYRHjcwl+T2Lez19YsVx+abhFmFFFh0dVB0tD5fm8ew5ng8+jULmsUkEVoA40L8v2gxcVOkVlVAkHKEm7H0GrNQF2HsYzX7/muDll0tRVAP7orXxiYbpQ1C3MwIDAgFVIOXZvHsOZ4PPo0aUZsqSEiz6ZXFx1Jqw0esUfsUzb5/wCYTK49NNDkI2KT861rWIfvhf3Ef+X/ALRPLc3j2HM8Hn0HfD3EJaUGWpQQrP4iFlwCShUsjMEqyKTmzpJSQ40vHfEHFJa0JlpUVkLBUolRBbOwClMtfKtKM5AJEsHaEEdHt8KOuzweLLh0PcbxuWsKCJZqKZgkMFLClIGWyciUoBFb2eLV8ckmYV5DlUEAjw5Y5Ek5pZqQQpJqoMXSkAAUGdjoOFEXGlY6HF5ZleEqXy5AlwlDghCBmBof+QLXf7XeJTONIKJssJWEqAEsDKAEhCkALpS+ely41eEcdFcKIuNI0A47LGdIl8i1KJGVH21zCa38ipSb/Y7RWOLSSqSooIEpGXKEI8xSxU5POxZYBFxW5UUcdE8KI+PI00rj2HDZpSlssKLy5Yzc6VeIWAZeUFGUcrG51A4pxRE2UhGVlpKCVZUJciXlX5a8yxm/KFEdAsUU7QPNJqjo6Ojo2Mjo6OjoAOjo6OgA6Ojo6ADo6OjoAOjo6OgA6Ojo6AAqTi8stUvIk5ieY3DgD5M46wSni7f/AEpTuTmYvVQU12bla1j7rI6E4IrXJDKTxYpIJloU24vRArSv/G/dR7GvE8Rz5v4ctOZATyizElx1r8hAMdC0IbnKhrO4ulWZ5EqpJqHZ9qaU9ABHDi8sO2GlV6aO7e4hVHROhF6pB+P4imalhKRLrmJTclmOliat2gCOjo0SSRjKTk9z/9k=");

        Park park5 = new Park().setAddress("Beit El 20-24, Tel Aviv-Yafo").setLat(32.117898).setLng(34.840876).setPid("park_5")
                .setWater("yes").setShade("yes").setLights("yes").setBenches("3")
                .setName("Neve Sharet Park")
                .setParkImage1("https://lh5.googleusercontent.com/p/AF1QipPaTPoSydU3_pUuCLr5lBL2EBShxvL4KApAW5k8=w426-h240-k-no")
                .setParkImage2("https://lh5.googleusercontent.com/p/AF1QipPQxInM2QE1EWIzj8qx6WSqdgU7INo2AlEzmXEN=w408-h544-k-no")
                .setParkImage3("https://res.cloudinary.com/swl42/image/upload/sw-server/spots/14849/-herzliya-gush-dan-street-workout-park.jpg")
                .setParkImage4("");


        mDatabas.child(park1.getPid()).setValue(park1);
        mDatabas.child(park2.getPid()).setValue(park2);
        mDatabas.child(park3.getPid()).setValue(park3);
        mDatabas.child(park4.getPid()).setValue(park4);
        mDatabas.child(park5.getPid()).setValue(park5);

    }


}
