package model.processor;

import lombok.Getter;
import model.Utils;
import model.memory.pages.VirtualPage;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Process {
    private int time;
    private final long id;
    private final List<VirtualPage> pages;

    public Process(long id, int time, List<VirtualPage> pages) {
        this.id = id;
        this.time = time;
        this.pages = pages;
    }

    public VirtualPage getRandomPage() {

        List<VirtualPage> presented = pages.stream().filter(VirtualPage::isPresent).collect(Collectors.toList());
        List<VirtualPage> notPresented = pages.stream().filter(x -> !x.isPresent()).collect(Collectors.toList());

        if(notPresented.isEmpty()){
            return presented.get(Utils.rand(0, presented.size()));
        }

        if(!presented.isEmpty() && Utils.rand(0, 10) < 9){
            return presented.get(Utils.rand(0, presented.size()));
        }

        return notPresented.get(Utils.rand(0, notPresented.size()));
    }

    public void decTime() {
        time--;
    }

    public boolean isModify(){
        return Utils.rand(0, 2) == 0;
    }

}
