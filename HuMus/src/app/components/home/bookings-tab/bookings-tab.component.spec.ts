import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingsTabComponent } from './bookings-tab.component';

describe('BookingsTabComponent', () => {
  let component: BookingsTabComponent;
  let fixture: ComponentFixture<BookingsTabComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BookingsTabComponent]
    });
    fixture = TestBed.createComponent(BookingsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
