import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BedInfoComponent } from './bed-info.component';

describe('BedInfoComponent', () => {
  let component: BedInfoComponent;
  let fixture: ComponentFixture<BedInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BedInfoComponent]
    });
    fixture = TestBed.createComponent(BedInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
