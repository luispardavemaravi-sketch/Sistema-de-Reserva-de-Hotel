import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { AuthService } from '../../core/services/auth/auth.service';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  template: `
    <main class="login-screen">
      <section class="login-brand" aria-label="Hotel Parda">
        <div class="login-logo">
          <span class="logo-mark"><span>P</span></span
          ><span
            ><strong>{{ admin.profile().name }}</strong
            ><small>PLATAFORMA HOTELERA</small></span
          >
        </div>
        <div class="brand-copy">
          <p class="brand-overline">CONTROL TOTAL EN CADA TURNO</p>
          <h1>Su hotel, en buenas manos.</h1>
          <p>Recepción, caja y ocupación en un solo lugar, a cualquier hora del día.</p>
        </div>
        <div class="brand-features">
          <div>
            <i class="pi pi-clock" aria-hidden="true"></i><strong>Estancias flexibles</strong
            ><small>Por horas o por noche</small>
          </div>
          <div>
            <i class="pi pi-receipt" aria-hidden="true"></i><strong>Comprobantes</strong
            ><small>Boletas y facturas</small>
          </div>
          <div>
            <i class="pi pi-wallet" aria-hidden="true"></i><strong>Caja por turno</strong
            ><small>Movimientos en tiempo real</small>
          </div>
          <div>
            <i class="pi pi-shield" aria-hidden="true"></i><strong>Acceso por cargo</strong
            ><small>Cada equipo ve lo necesario</small>
          </div>
        </div>
        <p class="brand-foot">
          <span class="live-dot"></span> Sistema operativo · Listo para recibir huéspedes
        </p>
      </section>
      <section class="login-form-panel">
        <button
          class="theme-switch"
          type="button"
          [attr.aria-label]="theme.dark() ? 'Activar modo claro' : 'Activar modo oscuro'"
          (click)="theme.toggle()"
        >
          <i [class]="theme.dark() ? 'pi pi-sun' : 'pi pi-moon'" aria-hidden="true"></i>
        </button>
        <div class="form-inner">
          <div class="form-heading">
            <p>BIENVENIDO A {{ admin.profile().name.toUpperCase() }}</p>
            <h2>Iniciar sesión</h2>
            <span>Ingrese con su cuenta y el turno que va a operar.</span>
          </div>
          <form [formGroup]="form" (ngSubmit)="submit()" novalidate>
            <label class="login-field"
              >Usuario, correo o DNI
              <div class="input-wrap">
                <i class="pi pi-user" aria-hidden="true"></i
                ><input
                  type="text"
                  formControlName="identifier"
                  autocomplete="username"
                  placeholder="nombre@hotel.com"
                  required
                /></div
            ></label>
            @if (
              (submitted() || form.controls.identifier.touched) && form.controls.identifier.invalid
            ) {
              <p class="field-error">Ingrese su usuario o correo.</p>
            }
            <label class="login-field"
              >Contraseña
              <div class="input-wrap">
                <i class="pi pi-lock" aria-hidden="true"></i
                ><input
                  [type]="showPassword() ? 'text' : 'password'"
                  formControlName="password"
                  autocomplete="current-password"
                  placeholder="Su contraseña"
                  required
                /><button
                  type="button"
                  class="eye"
                  [attr.aria-label]="showPassword() ? 'Ocultar contraseña' : 'Mostrar contraseña'"
                  (click)="showPassword.update((v) => !v)"
                >
                  <i
                    [class]="showPassword() ? 'pi pi-eye-slash' : 'pi pi-eye'"
                    aria-hidden="true"
                  ></i>
                </button></div
            ></label>
            @if (
              (submitted() || form.controls.password.touched) && form.controls.password.invalid
            ) {
              <p class="field-error">Ingrese su contraseña.</p>
            }
            <label class="login-field"
              >Turno de ingreso
              <div class="input-wrap">
                <i class="pi pi-calendar-clock" aria-hidden="true"></i
                ><select formControlName="turnoId" required>
                  @for (shift of auth.turnos(); track shift.idTurno) {
                    <option [ngValue]="shift.idTurno">
                      Turno {{ shift.idTurno }} · {{ shift.horaInicio }}–{{ shift.horaFinal }}
                    </option>
                  }
                </select>
              </div></label
            >
            <label class="remember"
              ><input type="checkbox" formControlName="remember" /> Mantener sesión al recargar esta pestaña
              de recepción</label
            >
            @if (error()) {
              <p class="login-error" role="alert">{{ error() }}</p>
            }
            <button class="submit-btn" type="submit" [disabled]="loading() || shiftsLoading() || !auth.turnos().length">
              @if (loading()) {
                <i class="pi pi-spin pi-spinner" aria-hidden="true"></i> Ingresando...
              } @else {
                Ingresar al panel <i class="pi pi-arrow-right" aria-hidden="true"></i>
              }
            </button>
          </form>
        </div>
      </section>
    </main>
  `,
  styles: [
    `
      :host {
        display: block;
        min-height: 100vh;
      }
      .login-screen {
        display: grid;
        grid-template-columns: minmax(390px, 1fr) minmax(450px, 1fr);
        min-height: 100vh;
        background: var(--surface);
        color: var(--ink);
      }
      .login-brand {
        position: relative;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        gap: 40px;
        padding: 44px clamp(30px, 5vw, 88px);
        background: linear-gradient(145deg, #0d1729, #172744 72%, #0e192e);
        color: #f3f7ff;
        overflow: hidden;
      }
      .login-brand:before {
        content: '';
        position: absolute;
        width: 65vw;
        height: 65vw;
        right: -42vw;
        top: 12vh;
        border: 1px solid #35568a;
        border-radius: 50%;
        box-shadow:
          0 0 0 68px #23395a40,
          0 0 0 138px #23395a25;
        pointer-events: none;
      }
      .login-logo,
      .brand-copy,
      .brand-features,
      .brand-foot {
        position: relative;
        z-index: 1;
      }
      .login-logo {
        display: flex;
        align-items: center;
        gap: 15px;
      }
      .logo-mark {
        display: grid;
        place-items: center;
        width: 48px;
        height: 48px;
        background: #436cf0;
        border-radius: 12px;
        transform: rotate(45deg);
        font-size: 25px;
        font-weight: 800;
      }
      .logo-mark > span {
        transform: rotate(-45deg);
      }
      .login-logo strong {
        display: block;
        font-size: 18px;
      }
      .login-logo small {
        display: block;
        margin-top: 4px;
        color: #a7bcdf;
        font-size: 11px;
        letter-spacing: 0.14em;
      }
      .brand-copy {
        max-width: 560px;
        margin: auto 0;
      }
      .brand-overline {
        color: #9eb9ff;
        font-size: 12px;
        font-weight: 800;
        letter-spacing: 0.13em;
      }
      .brand-copy h1 {
        font-size: clamp(42px, 5vw, 72px);
        line-height: 1.04;
        letter-spacing: -0.05em;
        margin: 18px 0 24px;
      }
      .brand-copy > p:last-child {
        font-size: 17px;
        line-height: 1.7;
        color: #bdcce2;
        max-width: 470px;
      }
      .brand-features {
        display: grid;
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 12px;
        max-width: 570px;
      }
      .brand-features > div {
        display: grid;
        grid-template-columns: 28px 1fr;
        gap: 3px 9px;
        padding: 15px;
        border: 1px solid #52658488;
        border-radius: 12px;
        background: #e5eff60d;
      }
      .brand-features i {
        grid-row: span 2;
        color: #94b5ff;
      }
      .brand-features strong {
        font-size: 12px;
      }
      .brand-features small {
        font-size: 11px;
        color: #b7c8de;
      }
      .brand-foot {
        font-size: 11px;
        color: #a7b9d2;
        margin: 0;
      }
      .live-dot {
        display: inline-block;
        width: 7px;
        height: 7px;
        background: #57c995;
        border-radius: 50%;
        margin-right: 6px;
      }
      .login-form-panel {
        position: relative;
        display: grid;
        place-items: center;
        padding: 70px 36px;
      }
      .form-inner {
        width: min(100%, 430px);
      }
      .theme-switch {
        position: absolute;
        right: 32px;
        top: 28px;
        width: 40px;
        height: 40px;
        border: 1px solid var(--line);
        border-radius: 50%;
        background: var(--raised);
        color: var(--ink);
      }
      .form-heading {
        margin-bottom: 32px;
      }
      .form-heading p {
        color: var(--login-accent);
        font-size: 11px;
        font-weight: 800;
        letter-spacing: 0.12em;
      }
      .form-heading h2 {
        font-size: 34px;
        letter-spacing: -0.04em;
        margin: 10px 0;
      }
      .form-heading span {
        font-size: 14px;
        color: var(--muted);
      }
      form {
        display: grid;
        gap: 15px;
      }
      .login-field {
        display: grid;
        gap: 8px;
        font-size: 13px;
        font-weight: 700;
      }
      .input-wrap {
        display: flex;
        align-items: center;
        gap: 12px;
        min-height: 46px;
        padding: 0 13px;
        border: 1px solid var(--control-line);
        border-radius: 9px;
        background: var(--control);
        color: var(--muted);
      }
      .input-wrap:focus-within {
        border-color: #7191ff;
      }
      .input-wrap > i {
        font-size: 15px;
      }
      .input-wrap input,
      .input-wrap select {
        width: 100%;
        min-width: 0;
        border: 0;
        outline: 0;
        background: transparent;
        color: var(--ink);
        font-size: 13px;
      }
      .input-wrap input::placeholder {
        color: var(--muted);
      }
      .eye {
        border: 0;
        background: none;
        color: var(--muted);
        padding: 6px;
      }
      .remember {
        display: flex;
        align-items: center;
        gap: 9px;
        color: var(--muted);
        font-size: 12px;
      }
      .remember input {
        accent-color: #426bf0;
      }
      .field-error,
      .login-error {
        color: #ed8d92;
        font-size: 12px;
        margin: -8px 0 0;
      }
      .submit-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        min-height: 48px;
        margin-top: 5px;
        border: 0;
        border-radius: 9px;
        background: #365fe0;
        color: #fff;
        font-weight: 800;
      }
      .submit-btn:hover {
        background: #2850c9;
      }
      @media (max-width: 950px) {
        .login-screen {
          grid-template-columns: 1fr;
        }
        .login-brand {
          min-height: 260px;
          padding: 25px 24px;
        }
        .brand-copy {
          margin: 0;
        }
        .brand-copy h1 {
          font-size: 36px;
        }
        .brand-copy > p:last-child {
          font-size: 13px;
        }
        .brand-features,
        .brand-foot {
          display: none;
        }
        .login-form-panel {
          padding: 45px 22px;
        }
        .theme-switch {
          top: 24px;
          right: 24px;
        }
      }
      @media (max-width: 500px) {
        .login-brand {
          min-height: 210px;
          gap: 20px;
        }
        .brand-copy h1 {
          font-size: 31px;
          margin: 7px 0;
        }
        .brand-overline {
          display: none;
        }
        .login-form-panel {
          padding: 35px 20px;
        }
        .form-heading h2 {
          font-size: 29px;
        }
      }
    `,
  ],
})
export class LoginComponent {
  readonly admin = inject(HotelAdminStore);
  readonly theme = inject(ThemeService);
  readonly auth = inject(AuthService);
  readonly shiftsLoading = signal(true);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  readonly showPassword = signal(false);
  readonly loading = signal(false);
  readonly submitted = signal(false);
  readonly error = signal('');
  readonly form = this.fb.nonNullable.group({
    identifier: ['', Validators.required],
    password: ['', Validators.required],
    turnoId: [0, [Validators.required, Validators.min(1)]],
    remember: [true],
  });
  constructor() {
    void this.loadTurnos();
  }
  async loadTurnos() {
    try {
      await this.auth.loadTurnos();
      this.form.controls.turnoId.setValue(this.auth.turnos()[0]?.idTurno ?? 0);
      if (!this.auth.turnos().length) this.error.set('No hay turnos configurados en el servidor.');
    } catch {
      this.error.set('No se pudieron cargar los turnos. Recargue la página para reintentar.');
    } finally {
      this.shiftsLoading.set(false);
    }
  }
  async submit() {
    this.submitted.set(true);
    if (this.form.invalid || this.loading()) return;
    this.loading.set(true);
    this.error.set('');
    try {
      await this.auth.login(this.form.getRawValue());
      await this.router.navigateByUrl(this.auth.landing());
    } catch (error) {
      this.error.set(error instanceof Error ? error.message : 'No se pudo iniciar sesión.');
    } finally {
      this.loading.set(false);
    }
  }
}
