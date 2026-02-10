CREATE OR REPLACE FUNCTION refresh_owner_maintenance(owner_id INT)
RETURNS VOID AS $$
BEGIN
    UPDATE OWNER_USER
    SET maintenance_paid = NOT EXISTS (
        SELECT 1
        FROM SITE
        WHERE owner_id = refresh_owner_maintenance.owner_id
          AND maintenance_paid = FALSE
    )
    WHERE owner_id = refresh_owner_maintenance.owner_id;
END;
$$ LANGUAGE plpgsql;