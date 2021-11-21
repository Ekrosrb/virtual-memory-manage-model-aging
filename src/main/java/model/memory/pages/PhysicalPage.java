package model.memory.pages;

import lombok.Getter;

@Getter
public class PhysicalPage extends Page {
    private Long pageNumber;

    public PhysicalPage(Long pageNumber){
        this.pageNumber = pageNumber;
    }
}
