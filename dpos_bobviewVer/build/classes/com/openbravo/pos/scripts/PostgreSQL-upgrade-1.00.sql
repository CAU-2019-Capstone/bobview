--    dPOS - Dongwun Point Of Sale
--    Copyright (c) 2009-2017 Dongwun & uniCenta & previous Openbravo POS works
--    http://www.dongwun.com
--
--    This file is part of dPOS.
--
--    dPOS is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    dPOS is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with dPOS.  If not, see <http://www.gnu.org/licenses/>.

-- Database upgrade script for PostgreSQL
-- Copyright (c) 2009-2017 Dongwun & uniCenta & previous Openbravo POS works
-- v1.00 - v1.10

UPDATE RESOURCES SET CONTENT = $FILE{/com/openbravo/pos/templates/Menu.Root.txt} WHERE ID = '0';

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};