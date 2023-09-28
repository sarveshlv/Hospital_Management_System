import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BedsTabComponent } from './beds-tab.component';

describe('BedsTabComponent', () => {
  let component: BedsTabComponent;
  let fixture: ComponentFixture<BedsTabComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BedsTabComponent]
    });
    fixture = TestBed.createComponent(BedsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
