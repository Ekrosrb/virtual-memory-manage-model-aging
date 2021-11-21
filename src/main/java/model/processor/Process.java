package model.processor;

import lombok.Getter;
import model.Utils;
import model.memory.pages.VirtualPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Process {
    private int time;
    private final long id;
    private final List<VirtualPage> pages;
    private final List<VirtualPage> active;

    public Process(long id, int time, List<VirtualPage> pages) {
        this.id = id;
        this.time = time;
        this.pages = pages;

        active = new ArrayList<>();

        pages.forEach(page -> {
            if(Utils.rand(0, 3) == 0){
                active.add(page);
            }
        });
        if(active.isEmpty()) {
            active.add(pages.get(0));
        }

    }

    public List<VirtualPage> getRandomPages(){
        return Stream.iterate(0, x -> x + 1).limit(2)
                .map(x -> getRandomPage())
                .collect(Collectors.toList());
    }

    public VirtualPage getRandomPage() {

        updateActive();

        if(Utils.rand(0, 10) < 9){
            return rand(active);
        }

        return rand(pages.stream()
                .filter(x -> !active.contains(x)).collect(Collectors.toList()));
    }

    private void updateActive(){
        int valid = Utils.rand(0, 10);
        if(valid <= 2 && active.size() > 1){
            active.remove(rand(active));
        }

        if(valid >= 8){
            pages.stream()
                    .filter(x -> !active.contains(x)).findFirst().ifPresent(active::add);
        }
    }

    private VirtualPage rand(List<VirtualPage> pages){
        if(pages.isEmpty()){
            return this.pages.get(Utils.rand(0, this.pages.size()));
        }
        return pages.get(Utils.rand(0, pages.size()));
    }

    public void decTime() {
        time--;
    }

    public boolean isModify(){
        return Utils.rand(0, 2) == 0;
    }

}
