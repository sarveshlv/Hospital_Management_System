import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HospitalsTabComponent } from './hospitals-tab.component';

describe('HospitalsTabComponent', () => {
  let component: HospitalsTabComponent;
  let fixture: ComponentFixture<HospitalsTabComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HospitalsTabComponent]
    });
    fixture = TestBed.createComponent(HospitalsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
