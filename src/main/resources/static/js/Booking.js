(() => {
    const els = {
        form: document.getElementById('bookingForm'),
        name: document.getElementById('name'),
        email: document.getElementById('email'),
        showId: document.getElementById('showId'),
        seatGrid: document.getElementById('seatGrid'),
        seatMeta: document.getElementById('seatMeta'),            // NYT: overview-label
        bookBtn: document.getElementById('bookBtn'),
        clearBtn: document.getElementById('clearBtn'),
        status: document.getElementById('status'),
        confirmation: document.getElementById('confirmation')
    };

    let theaterRows = 0;
    let seatsPerRow = 0;
    let selected = new Set(); // "row-seat" strings

    // --------------- Helpers ---------------
    const key = (r, s) => `${r}-${s}`;
    const parseKey = ks => ks.split('-').map(Number);

    function setStatus(msg, type = 'info') {
        if (!els.status) return;
        els.status.textContent = msg ?? '';
        els.status.className = `status ${type}`;
    }

    function showConfirmation(dto) {
        if (!els.confirmation) return;
        els.confirmation.hidden = false;
        els.confirmation.textContent = JSON.stringify(dto, null, 2);
    }

    function clearConfirmation() {
        if (!els.confirmation) return;
        els.confirmation.hidden = true;
        els.confirmation.textContent = '';
    }

    function validate() {
        const baseValid = els.form.checkValidity();
        const hasSeat = selected.size > 0;
        els.bookBtn.disabled = !(baseValid && hasSeat);
        return baseValid && hasSeat;
    }

    function seatButton(r, s, taken) {
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'seat';
        btn.dataset.row = String(r);
        btn.dataset.seat = String(s);
        btn.textContent = s;

        if (taken) {
            btn.classList.add('is-taken');
            btn.disabled = true;
        }

        btn.addEventListener('click', () => {
            const k = key(r, s);
            if (selected.has(k)) {
                selected.delete(k);
                btn.classList.remove('selected');
            } else {
                selected.add(k);
                btn.classList.add('selected');
            }
            validate();
        });

        return btn;
    }

    // --------------- Data loaders ---------------
    async function loadShows() {
        const sel = els.showId;
        sel.innerHTML = '<option value="">Loading…</option>';
        try {
            const res = await fetch('/api/shows');
            if (!res.ok) throw new Error(`GET /api/shows ${res.status}`);
            const shows = await res.json();
            sel.innerHTML = '';
            if (!Array.isArray(shows) || shows.length === 0) {
                sel.innerHTML = '<option value="">No shows</option>';
                return;
            }
            for (const s of shows) {
                const opt = document.createElement('option');
                // VIGTIGT: vores ShowController returnerer "id"
                opt.value = s.id;
                const when = new Date(s.startTime).toLocaleString();
                opt.textContent = `${s.movieTitle} — ${when} — ${s.theaterName}`;
                sel.appendChild(opt);
            }
            sel.dispatchEvent(new Event('change'));
        } catch (e) {
            console.error(e);
            sel.innerHTML = '<option value="">Error loading shows</option>';
            setStatus('Could not load shows.', 'error');
        }
    }

    async function loadShowInfoAndSeats(showId) {
        if (!showId) return;
        setStatus('Loading seats…', 'info');
        selected.clear();
        clearConfirmation();
        els.seatGrid.innerHTML = '';

        try {
            // 1) Hent show-info (rows, seatsPerRow)
            const infoRes = await fetch(`/bookings/show-info?showId=${encodeURIComponent(showId)}`);
            if (!infoRes.ok) throw new Error(`GET /bookings/show-info ${infoRes.status}`);
            const info = await infoRes.json();
            theaterRows = info.theaterRows;
            seatsPerRow = info.seatsPerRow;

            if (els.seatMeta) {
                els.seatMeta.textContent = `Rows: ${theaterRows} • Seats/row: ${seatsPerRow}`;
            }

            // 2) Hent optagne sæder
            const occRes = await fetch(`/bookings/occupied?showId=${encodeURIComponent(showId)}`);
            if (!occRes.ok) throw new Error(`GET /bookings/occupied ${occRes.status}`);
            const occupied = await occRes.json(); // [{row, seat}, ...]
            const takenSet = new Set(occupied.map(o => key(o.row, o.seat)));

            // 3) Render grid
            const frag = document.createDocumentFragment();
            for (let r = 1; r <= theaterRows; r++) {
                const rowDiv = document.createElement('div');
                rowDiv.className = 'seat-row';
                const label = document.createElement('span');
                label.className = 'row-label';
                label.textContent = `Row ${r}`;
                rowDiv.appendChild(label);

                for (let s = 1; s <= seatsPerRow; s++) {
                    rowDiv.appendChild(seatButton(r, s, takenSet.has(key(r, s))));
                }
                frag.appendChild(rowDiv);
            }
            els.seatGrid.appendChild(frag);
            setStatus('Pick seats and book.', 'info');
            validate();
        } catch (e) {
            console.error(e);
            setStatus('Failed to load seat map.', 'error');
        }
    }

    // --------------- Submit booking ---------------
    async function submitBooking(evt) {
        evt.preventDefault();
        if (!validate()) {
            setStatus('Please fill out all fields and select at least one seat.', 'error');
            return;
        }

        const showId = Number(els.showId.value);
        const seats = Array.from(selected).map(k => {
            const [row, seat] = parseKey(k);
            return { row, seat };
        });

        const payload = {
            customerName: els.name.value.trim(),
            customerEmail: els.email.value.trim(),
            showId,
            seats
        };

        els.bookBtn.disabled = true;
        setStatus('Booking…', 'info');

        try {
            const res = await fetch('/bookings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.status === 201) {
                const data = await res.json();
                setStatus('Booking successful ✅', 'success');

                showConfirmation(data);

                selected.clear();
                await loadShowInfoAndSeats(showId);
                els.form.reset();
            } else if (res.status === 409) {
                const err = await res.json();
                setStatus(err.message || 'Seat(s) already booked', 'error');

                // marker konflikter hvis backend sender en liste
                if (Array.isArray(err.conflicts)) {
                    err.conflicts.forEach(c => {
                        const btn = els.seatGrid.querySelector(
                            `.seat[data-row="${c.row}"][data-seat="${c.seat}"]`
                        );
                        if (btn) {
                            btn.classList.add('conflict');
                            selected.delete(key(c.row, c.seat));
                        }
                    });
                }
                validate();
            } else if (res.status === 400) {
                const err = await res.json().catch(() => ({}));
                setStatus(err.message || 'Validation error', 'error');
            } else {
                setStatus(`Unexpected error (${res.status})`, 'error');
            }
        } catch (e) {
            console.error(e);
            setStatus('Network error while booking', 'error');
        } finally {
            validate(); // re-enable knap hvis muligt
        }
    }

    // --------------- Events ---------------
    document.addEventListener('DOMContentLoaded', () => {
        if (els.showId && els.showId.options.length > 0 && els.showId.value) {
            loadShowInfoAndSeats(els.showId.value);
        } else {
            loadShows();
        }

        els.showId.addEventListener('change', e => {
            const v = e.target.value;
            selected.clear();
            loadShowInfoAndSeats(v);
        });

        if (els.clearBtn) {
            els.clearBtn.addEventListener('click', () => {
                selected.clear();
                // fjern valgt-styling
                els.seatGrid.querySelectorAll('.seat.selected').forEach(b => b.classList.remove('selected'));
                clearConfirmation();
                setStatus('');
                validate();
            });
        }

        // real-time form validation
        ['input', 'change'].forEach(ev => {
            els.form.addEventListener(ev, validate);
        });

        els.form.addEventListener('submit', submitBooking);
        validate();
    });
})();