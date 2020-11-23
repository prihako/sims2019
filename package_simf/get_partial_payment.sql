create or replace PROCEDURE         get_partial_payment (
                    in_nBiId    IN NUMBER,
                    out_nType   OUT NUMBER,
                    out_nAmount OUT NUMBER) IS
 sCustomer common_settings.com_customer_name%type := General_Settings.Get_Customer;
 rBilling billing%rowtype;
BEGIN
   select * into rBilling
     from billing
    where bi_id = in_nBiId;

    out_nType := rBilling.bi_instalment_type;

    If out_nType is null then
       if sCustomer = 'POSTEL' then
          out_nType := 2;
          out_nAmount :=0;
       else
          out_nType := 1;
          out_nAmount := 0;
       end if;
    Elsif out_nType = 3 then
       out_nAmount := round(rBilling.bi_cost_bill / rBilling.bi_instalment_times, General_Settings.Get_Decimal_Digit);
    Elsif out_nType = 4 then
        select bii_amount
        into out_nAmount
		 from billing_instalment
		where bi_bi_id = in_nBiId and
		      bii_money_received is null and
 			  bii_id=(select min (bii_id) from biLling_instalment where bi_bi_id = in_nBiId and bii_money_received is null ) -- edited by sigma
       order by bii_id;
    End If;
Exception when no_data_found then out_nAmount := 0;
END;