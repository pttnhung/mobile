package com.example.listview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Integer>> numberList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<Integer>> getNumberList() {
        return numberList;
    }
    public void setNumberList(ArrayList<Integer> newList) {
        numberList.setValue(newList);
    }



    // Tăng thêm 1 số mới vào list
    public void increaseNumber() {
        ArrayList<Integer> currentList = numberList.getValue();
        if (currentList == null) currentList = new ArrayList<>();
        int newValue = currentList.size() > 0 ? currentList.get(currentList.size() - 1) + 1 : 1;
        currentList.add(newValue);
        numberList.setValue(currentList);
    }

    // Sửa số ở vị trí index
    public void updateNumber(int position, int newValue) {
        ArrayList<Integer> currentList = numberList.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.set(position, newValue);
            numberList.setValue(new ArrayList<>(currentList)); // tạo list mới để kích hoạt observer
        }
    }
}
