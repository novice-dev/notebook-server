import pickle, sys
from types import ModuleType

#returns a 'bag' of variables defined in the global scope that are not built-in, nor callable, nor modules
def get_session_bag():
  session_bag = {}
  tmp = globals().copy()
  for k,v in tmp.items():
    if (not k.startswith('__') and k != 'tmp' and k != 'In' and k != 'Out' and not hasattr(v, '__call__') and not isinstance(v, ModuleType)):
      session_bag[k] = v
  return session_bag

def unpickle_session(session):
  with open(session, 'rb') as file:
    session_bag = pickle.load(file)
    globals().update(session_bag)

def pickle_session(session):
  session_bag = get_session_bag()
  with open(session, 'wb') as file:
    pickle.dump(session_bag, file)

if (len(sys.argv) == 2):
  unpickle_session(sys.argv[1])

