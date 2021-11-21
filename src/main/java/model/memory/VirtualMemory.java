package model.memory;

import lombok.Getter;
import model.Log;
import model.memory.pages.Page;
import model.memory.pages.VirtualPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.Log.LogType.*;

@Getter
public class VirtualMemory {
    private final List<VirtualPage> memory;
    public VirtualMemory(){
        memory = new ArrayList<>();
    }

    public long getSize() {
        return memory.size()*Page.SIZE;
    }

    public List<VirtualPage> getPresentPages(){
        return memory.stream().filter(VirtualPage::isPresent).collect(Collectors.toList());
    }

    public void addPages(List<VirtualPage> pages){
        List<Long> numbers = Stream.iterate(0L, x -> x + 1L)
                .limit(memory.size() + pages.size())
                .filter(x -> memory.stream().noneMatch(page -> page.getPageNumber() == x))
                .limit(pages.size()).collect(Collectors.toList());

        for(int i = 0; i < pages.size(); i++){
            pages.get(i).setPageNumber(numbers.get(i));
        }
        memory.addAll(pages);

        Log.info(VIRTUAL_MEMORY, "Add " + pages.size() + " pages. Size: " + memory.size());
    }

    public void removePages(List<VirtualPage> pages){
        memory.removeAll(pages);
        Log.info(VIRTUAL_MEMORY, "Removed " + pages.size() + " pages. Size: " + memory.size());
    }
}
