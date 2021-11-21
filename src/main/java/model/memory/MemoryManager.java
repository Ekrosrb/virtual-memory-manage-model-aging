package model.memory;

import lombok.Getter;
import model.Log;
import model.OS;
import model.PrintUtils;
import model.Utils;
import model.memory.pages.PhysicalPage;
import model.memory.pages.VirtualPage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.Log.LogType.*;

@Getter
public class MemoryManager {

    private final PhysicalMemory physicalMemory;
    private final VirtualMemory virtualMemory = new VirtualMemory();

    private static final long LAST_BIT = 32768;

    public MemoryManager(long physicalMemoryPages){
        physicalMemory = new PhysicalMemory(physicalMemoryPages);
    }

    public void execute(VirtualPage virtualPage, boolean isModify){

        if(!virtualPage.isPresent()){
            Log.info(MEMORY_MANAGER, "|X|Page fault|X|");
            bestPhysical(virtualPage);

            OS.COUNTER.incPageFault();
        }
        if(!virtualPage.isReference()) {
            virtualPage.setReference(true);
        }
        if(isModify){
            virtualPage.setModify(true);
        }
    }

    private void bestPhysical(VirtualPage virtualPage){
        List<PhysicalPage> freePages = getFree();

        Long number = !freePages.isEmpty()
                ? freePages.get(0).getPageNumber()
                : writeToFS(virtualMemory.getMemory().stream().filter(VirtualPage::isPresent)
                .min(Comparator.comparingLong(VirtualPage::getPriority)).orElseThrow());

        Log.info(AGING, "Set physical page " + number
                + " to virtual page " + virtualPage.getPageNumber());

        virtualPage.setPhysicalPageNumber(number);
        virtualPage.setPresent(true);
        virtualPage.setInFs(false);
    }

    public void updateReference(){
        update();
        virtualMemory.getMemory().stream()
                .filter(VirtualPage::isPresent)
                .filter(VirtualPage::isReference)
                .collect(Collectors.toList())
                .forEach(page -> page.setReference(Utils.rand(0, 3) != 1));
        Log.info(BACKGROUND_PROCESS, "Update references");
    }

    private Long writeToFS(VirtualPage page){

        Log.info(AGING, "Write " + page.getPageNumber() + " to FS. Page PPN - "
                + page.getPhysicalPageNumber() + " Page aging - "
                + PrintUtils.formatBinary16(Long.toBinaryString(page.getPriority())));

        OS.COUNTER.incWriteToFSCount();

        Long ppn = page.getPhysicalPageNumber();
        page.setPhysicalPageNumber(null);
        page.setPresent(false);
        page.setReference(false);
        page.setModify(false);
        page.setInFs(true);
        page.setPriority(0L);
        return ppn;
    }

    public void update(){
        Log.info(AGING, "Update aging");
        virtualMemory.getMemory().forEach(page->{
            if(page.isPresent()){
                page.setPriority((page.getPriority() >> 1) + (page.isReference() ? LAST_BIT: 0));
            }
        });
    }

    private List<PhysicalPage> getFree(){
       return physicalMemory.getMemory().stream()
               .filter(pPage -> virtualMemory.getMemory().stream()
                       .noneMatch(vPage -> Objects.equals(vPage.getPhysicalPageNumber(), pPage.getPageNumber())))
               .collect(Collectors.toList());
    }

    public void removePages(List<VirtualPage> pages){
        virtualMemory.removePages(pages);
    }

}

