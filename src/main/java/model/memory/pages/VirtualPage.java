package model.memory.pages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VirtualPage extends Page {
    private long pageNumber;
    private boolean present;
    private boolean reference;
    private boolean modify;
    private Long physicalPageNumber;

    private long priority;

    public VirtualPage(long pageNumber){
        this.pageNumber = pageNumber;
        present = false;
        reference = false;
        modify = false;
        physicalPageNumber = null;
    }

}
