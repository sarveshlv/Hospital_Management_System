import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HospitalProfileComponent } from './hospital-profile.component';

describe('HospitalProfileComponent', () => {
  let component: HospitalProfileComponent;
  let fixture: ComponentFixture<HospitalProfileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HospitalProfileComponent]
    });
    fixture = TestBed.createComponent(HospitalProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
