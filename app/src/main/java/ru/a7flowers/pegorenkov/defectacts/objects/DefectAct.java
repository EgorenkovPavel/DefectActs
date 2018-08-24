package ru.a7flowers.pegorenkov.defectacts.objects;

import java.util.ArrayList;
import java.util.List;

public class DefectAct {

    private List<Defect> defects = new ArrayList<>();

    public List<Defect> getDefects() {
        return defects;
    }

    public void setDefects(List<Defect> defects) {
        this.defects = defects;
    }

    public void addDefect(Defect defect){
        defects.add(defect);
    }
}
