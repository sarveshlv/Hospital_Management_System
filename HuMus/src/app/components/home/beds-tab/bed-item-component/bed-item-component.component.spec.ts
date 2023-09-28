import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BedItemComponentComponent } from './bed-item-component.component';

describe('BedItemComponentComponent', () => {
  let component: BedItemComponentComponent;
  let fixture: ComponentFixture<BedItemComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BedItemComponentComponent]
    });
    fixture = TestBed.createComponent(BedItemComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
