"""Re-runs every idempotent mass pass (use after merging other branches). Usage: python tools/reapply.py"""
import subprocess
import sys

STEPS = ['fix_imports.py', 'mass2.py', 'mass3.py', 'mass4.py', 'mass5.py', 'mass6.py', 'mass7.py', 'mass8.py',
         'mass9.py', 'unwrap_selector.py', 'mass10.py', 'mass11.py', 'mass12.py', 'mass13.py', 'ensure_imports.py']
for step in STEPS:
    r = subprocess.run([sys.executable, 'tools/' + step], capture_output=True, text=True)
    last = (r.stdout.strip().splitlines() or [''])[0]
    print('%-22s %s%s' % (step, last, '' if r.returncode == 0 else '  !! ' + r.stderr.strip().splitlines()[-1]))
