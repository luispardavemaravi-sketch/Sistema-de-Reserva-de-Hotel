import { TestBed } from '@angular/core/testing';
import { Subject, of } from 'rxjs';
import { vi } from 'vitest';
import { HttpErrorResponse } from '@angular/common/http';
import { ReservasComponent } from './reservas.component';
import { BackendReservation, ReservaBackendService } from '../../core/services/reserva/reserva-backend.service';

describe('Reservation HTTP feedback', () => {
  it('waits for the server, prevents duplicate actions and reports failure without success', async () => {
    const response = new Subject<BackendReservation>();
    const api = { rooms: () => of([]), guests: () => of([]), reservations: () => of([]), transition: vi.fn(() => response) };
    TestBed.configureTestingModule({ providers: [{ provide: ReservaBackendService, useValue: api }] });
    const component = TestBed.runInInjectionContext(() => new ReservasComponent());
    await Promise.resolve();
    const row = { id: 1, code: 'TEST', guest: 'Test', room: '101', from: '', to: '', status: 'Pendiente', extras: [], total: 0 };
    const action = component.confirm(row);
    await component.confirm(row);
    expect(api.transition).toHaveBeenCalledTimes(1);
    expect(component.saving()).toBe(true);
    expect(component.notice()).toBe('');
    response.error(new HttpErrorResponse({ status: 409 }));
    await action;
    expect(component.saving()).toBe(false);
    expect(component.notice()).toBe('');
    expect(component.error()).toContain('409');
  });
});
