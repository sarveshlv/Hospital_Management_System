import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddHospitalComponent } from './add-hospital.component';

describe('AddHospitalComponent', () => {
  let component: AddHospitalComponent;
  let fixture: ComponentFixture<AddHospitalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddHospitalComponent]
    });
    fixture = TestBed.createComponent(AddHospitalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
