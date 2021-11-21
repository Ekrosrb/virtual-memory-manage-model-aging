package model.memory;

import lombok.Getter;
import model.memory.pages.Page;
import model.memory.pages.PhysicalPage;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PhysicalMemory {
    private List<PhysicalPage> memory;
    private long size;
    public PhysicalMemory(long pages){

        size = pages * Page.SIZE;

        memory = new ArrayList<>();

        for(long i = 0; i < pages; i++){
            memory.add(new PhysicalPage(i));
        }
    }
}
